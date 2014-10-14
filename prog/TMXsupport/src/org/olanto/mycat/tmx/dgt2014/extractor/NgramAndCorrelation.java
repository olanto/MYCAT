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
package org.olanto.mycat.tmx.dgt2014.extractor;

import org.olanto.mycat.tmx.common.ItemsCorrelation;
import java.rmi.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.olanto.conman.server.GetContentService;
import org.olanto.idxvli.server.*;
import org.olanto.idxvli.util.SetOperation;
import org.olanto.mycat.tmx.common.ItemsCorrelation;
import org.olanto.mycat.tmx.dgt2014.extract.LangMapDGT2014;
import org.olanto.mysqd.server.MySelfQuoteDetection;
import org.olanto.mysqd.util.Ref;
import static org.olanto.util.Messages.*;
import org.olanto.util.Timer;

/**
 * test une recherche
 *
 */
public class NgramAndCorrelation {

    static IndexService_MyCat is;
    final static float NTOT = 100000000;
    static HashMap<String, String> stopword;
    static boolean SEQUENCE = true;
    static int NB_EXAMPLES = 1;

    public static void main(String[] args) {

        initIS();


//test("armement OR nucléaire");
//test("weapon IN[\"SOURCE.EN\" AND \"TARGET.RU\"]");
//test("(weapon AND nuclear)IN[\"SOURCE.EN\" AND \"TARGET.RU\"]");
//test("NEAR(\"weapon\",\"nuclear\")IN[\"SOURCE.EN\" AND \"TARGET.RU\"]");
//test("QUOTATION(\"efforts to mainstream gender perspectives\") IN[\"SOURCE.EN\" AND \"TARGET.FR\"]");
//test("QUOTATION(\"financial the collappse\") ");

//        test("QUOTATION(\"final report\") IN[\"SOURCE.EN\"]");
//       test("QUOTATION(\"rapport final\") IN[\"SOURCE.FR\"]");

//        correlation("rapport final", "final report", "FR", "EN");
//        correlation("rapport final", "finax report", "FR", "EN");
//        correlation("rapport", "report", "FR", "EN");
//        correlation("table", "table", "FR", "EN");
//        correlation("voiture", "car", "FR", "EN");
//        correlation("menu", "menu", "FR", "EN");
//        correlation("Annulation du jugement", "mistrial", "FR", "EN");
//        correlation("amende", "Financial penalty", "FR", "EN");
//        correlation("amende", "penalty", "FR", "EN");
//        correlation("appel", "appeal", "FR", "EN");
//        correlation("assistance judiciaire", "legal assistance", "FR", "EN");
//
        try {
            msg(is.getDoc(0));
            msg(is.getDoc(1));
        } catch (RemoteException ex) {
            Logger.getLogger(NgramAndCorrelation.class.getName()).log(Level.SEVERE, null, ex);
        }

        // test corpus ONU
//        getTranslation("rapport final", "FR", "EN", 200, 2);
//        getTranslation("Détention illégale", "FR", "EN", 10, 2);
//        getTranslation("riz", "FR", "EN", 200, 1);
//        getTranslation("assistance judiciaire", "FR", "EN", 50, 2);

        // test corpus WIKI
//        getTranslation("océan atlantique", "FR", "EN", 10, 2);
//        getTranslation("océan atlantique", "FR", "EN", 10, 1);
//        getTranslation("otan", "FR", "EN", 10, 1);
        getTranslation("genève", "FR", "EN", 5, 1);

//       getTranslation("prix nobel", "FR", "EN", 100, 2);
//       getTranslation("lauréat prix nobel", "FR", "EN", 100, 2);

        //      getTranslation("guerre", "FR", "EN", 50, 1);
//       getTranslation("noir", "FR", "EN", 100, 1);
//       getTranslation("vendre", "FR", "EN", 4, 1);
//       getTranslation("échecs", "FR", "EN", 20, 1);
//       getTranslation("bombe atomique", "FR", "EN", 4, 1);
//       getTranslation("frites", "FR", "EN", 2, 1);
//       getTranslation("chat", "FR", "EN", 10, 1);
//         getTranslation("poisson chat", "FR", "EN", 10, 1);
//         getTranslation("courriel", "FR", "EN", 3, 1);
//       getTranslation("invalide", "FR", "EN", 2, 1);

    }

    public static void initIS() {
        if (is == null) {
            is = GetContentService.getServiceMYCAT("rmi://localhost/VLI");
        }
//        try {
//            showVector(is.getDictionnary().result);
//            showVector(is.getCorpusLanguages());
//        } catch (RemoteException ex) {
//            ex.printStackTrace();
//        }
    }

    public static void initIS(IndexService_MyCat _is) {
        is = _is;
    }

    static void test(String query) {
        try {
            Timer t1 = new Timer("------------- " + query);
            QLResultNice res = is.evalQLNice(query, 0, 200);
            msg("time:" + res.duration);
            msg("nbres:" + res.result.length);
            for (int i = 0; i < res.result.length; i++) {
                msg(i + "  docid: " + res.result[i]);
                msg("  docname: " + res.docname[i]);
                msg("");
            }
            t1.stop();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    private static void init() {
        try {
            stopword = new HashMap<>(2000);
            String[] stoplist = is.getStopWords();
            for (int i = 0; i < stoplist.length; i++) {
                stopword.put(stoplist[i], "OK");
            }
        } catch (RemoteException ex) {
            Logger.getLogger(NgramAndCorrelation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static boolean checkFistAndLastNotStopWord(String ngram) {
        if (stopword == null) {
            init();
        }
        String[] terms = ngram.split(" ");
        if (stopword.get(terms[0]) != null) {
            return false;
        }
        if (stopword.get(terms[terms.length - 1]) != null) {
            return false;
        }

        return true;
    }

    public static String getAndExpression(String expression) {
        if (stopword == null) {
            init();
        }
        expression = expression.replace(":", " ");
        expression = expression.replace("/", " ");
        expression = expression.replace("&", " ");
        expression = expression.replace(";", " ");
        expression = expression.replace("[", " ");
        expression = expression.replace("  ", " ");

        String res = "";
        String[] terms = expression.split(" ");
        for (int i = 0; i < terms.length; i++) {
            if (stopword.get(terms[i]) == null && terms[i].length() > 1) {
                if (res.length() > 1) {
                    res += " AND ";
                }
                res += terms[i];
            }
        }
        return res;
    }

    public static void getTranslation(String termso, String langso, String langta, int minFreq, int minLength) {
        getComposite(termso, langso, langta, minFreq, minLength);
        String target = getTarget(termso, langso, langta);
        List<Ref> ref = getNGram(target, minFreq, minLength, minLength+4);
        for (int i = 0; i < ref.size(); i++) { // pour chaque n-gram
            msg(correlation(termso, ref.get(i).ngram, langso, langta));
        }
    }

    public static void getComposite(String termso, String langso, String langta, int minFreq, int minLength) {
        String source = getSource(termso, langso, langta);
        List<Ref> ref = getNGram(source, minFreq, minLength, minLength+3);
        msg("------ composite terms for: " + termso);
        for (Ref r : ref) { // pour chaque n-gram
            msg(r.ngram + " (" + r.nbocc + ")");
        }
    }

    public static List<Ref> getNGram(String content, int minFreq, int minLength, int maxLength) {
        List<Ref> allref = getRawNGram(content, minFreq, minLength, maxLength);
        List<Ref> reducedRef = new Vector<>();
        for (int i = 0; i < allref.size(); i++) { // pour chaque n-gram
            Ref r = allref.get(i);
            if (r.len <= maxLength) {
                if (checkFistAndLastNotStopWord(r.ngram)) {
                    // System.out.println(r.ngram + ", " + r.nbocc + ", " + checkFistAndLastNotStopWord(r.ngram));
                    reducedRef.add(r);
                }
            }
        }
        return reducedRef;

    }

    public static List<Ref> getNGramIncluded(String content, int minFreq, int minLength, String checkInclude) {
        List<Ref> allref = getRawNGram(content, minFreq, minLength, minLength + 3);
        List<Ref> reducedRef = new Vector<>();
        for (int i = 0; i < allref.size(); i++) { // pour chaque n-gram
            Ref r = allref.get(i);
            if (r.len <= minLength + 3) {
                if (checkFistAndLastNotStopWord(r.ngram) && r.ngram.contains(checkInclude)) {
                    //System.out.println(r.ngram + ", " + r.nbocc + ", " + checkFistAndLastNotStopWord(r.ngram));
                    reducedRef.add(r);
                }
            }
        }
        return reducedRef;

    }

    public static List<Ref> getRawNGram(String content, int minFreq, int minLength, int maxLength) {
        MySelfQuoteDetection mysqd = new MySelfQuoteDetection(content, minFreq, minLength, maxLength);
        return mysqd.getNGram();
    }

    public static int getFrequency(String termso, String langso, String langta) {
        try {
            String queryso = getQuery(termso, langso, langta);
            QLResultNice resso = is.evalQLNice(queryso, 0, 0);
            return resso.result.length;
        } catch (RemoteException ex) {
            //  ex.printStackTrace();
            return 0;
        }
    }

    public static int fixMinFreq(int freqQ) {
        int minFreq = 2;
        if (freqQ < 10) {
            return minFreq;
        }
        return Math.max(minFreq, freqQ / 10);
    }

    public static int fixMinTerm(String query) {
        String[] part = query.split(" ");
        if (part == null) {
            return 2;
        }
        return Math.max(2, part.length + 1);
    }

    public static String getSource(String termso, String langso, String langta) {
        StringBuilder sourceTXT = new StringBuilder("");
        try {
            String queryso = getQuery(termso, langso, langta);
            Timer t1 = new Timer("------------- " + queryso);
            QLResultNice resso = is.evalQLNice(queryso, 0, 0);
            msg("n1:" + resso.result.length);
            for (int i = 0; i < resso.result.length; i++) { // 
                sourceTXT.append(is.getDoc(resso.result[i])).append("\n");
            }
            msg("length source:" + sourceTXT.length());
            t1.stop();
            return sourceTXT.toString();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return " ";
        }
    }

    public static String getTarget(String termso, String langso, String langta) {
        StringBuilder targetTXT = new StringBuilder("");
        try {
            String queryso = getQuery(termso, langso, langta);
            Timer t1 = new Timer("------------- " + queryso);
            QLResultNice resso = is.evalQLNice(queryso, 0, 0);
            float n1 = resso.result.length;
            msg("n1:" + n1);
            for (int i = 0; i < resso.result.length; i++) { // adjust value to source

                //System.out.println(i+"SO:"+is.getDoc(resso.result[i]));
                //System.out.println((i- LangMap.deltaSOTA(langso, langta))+"TA:"+is.getDoc(resso.result[i]- LangMap.deltaSOTA(langso, langta)));
                targetTXT.append(is.getDoc(resso.result[i] - LangMapDGT2014.deltaSOTA(langso, langta))).append("\n");
            }
            msg("length target:" + targetTXT.length());
            t1.stop();
            String allSentence=targetTXT.toString();
            return allSentence;
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return " ";
        }
    }

    public static String correlation(String termso, String termta, String langso, String langta) {
        String res = "";
        try {
            String queryso = getQuery(termso, langso, langta);
            String queryta = getQuery(termta, langta, langso);
            //Timer t1 = new Timer("------------- " + queryso);
            QLResultNice resso = is.evalQLNice(queryso, 0, 0);
            QLResultNice resta = is.evalQLNice(queryta, 0, 0);
//            msg("time:" + resso.duration);
            float n1 = resso.result.length;
//            msg("n1:" + resso.result.length);
//            msg("time:" + resta.duration);
            float n2 = resta.result.length;
//            msg("n2:" + resta.result.length);
            for (int i = 0; i < resta.result.length; i++) { // adjust value to source
                resta.result[i] += LangMapDGT2014.deltaSOTA(langso, langta);
            }
            int[] interserct = SetOperation.and(resso.result, resta.result);
//            msg("n12:" + interserct.length);
            double n12 = interserct.length;
            double num = NTOT * n12 - n1 * n2;
            double den = Math.sqrt(NTOT * n1 - n1 * n1) * Math.sqrt(NTOT * n2 - n2 * n2);
//            msg("den:" + den);
            double corelation = 0;
            if (den != 0) {
                corelation = num / den;
            }

            res = termso + "<->" + termta + " =corelation:" + corelation
                    + ", n1:" + resso.result.length
                    + ", n2:" + resta.result.length
                    + ", n12:" + interserct.length;
            //    t1.stop();
            return res;
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return res;
        }
    }

    public static String getQuery(String termso, String langso, String langta) {
        if (SEQUENCE) {
            return "QUOTATION(\"" + termso + "\") IN[\"SOURCE." + langso + "\" ANDL \"TARGET." + langta + "\"]";
        }
        return getAndExpression(termso) + " IN[\"SOURCE." + langso + "\" ANDL \"TARGET." + langta + "\"]";
    }

    public static ItemsCorrelation correlationObj(String termso, String termta, String langso, String langta) {
        String res = "";
        try {
            String queryso = getQuery(termso, langso, langta);
            String queryta = getQuery(termta, langta, langso);
            Timer t1 = new Timer("------------- " + queryso + " <--> " + queryta);
            QLResultNice resso = is.evalQLNice(queryso, 0, 0);
            QLResultNice resta = is.evalQLNice(queryta, 0, 0);
            //msg("timeQ1:" + resso.duration);
            float n1 = resso.result.length;
            //msg("n1:" + resso.result.length);
            //msg("timeQ2:" + resta.duration);
            float n2 = resta.result.length;
            //msg("n2:" + resta.result.length);
            int deltaSOTA = LangMapDGT2014.deltaSOTA(langso, langta);
            for (int i = 0; i < resta.result.length; i++) { // adjust value to source
                resta.result[i] += deltaSOTA;
            }
            int[] interserct = SetOperation.and(resso.result, resta.result);
            //msg("n12:" + interserct.length);
            double n12 = interserct.length;
            double num = NTOT * n12 - n1 * n2;
            double den = Math.sqrt(NTOT * n1 - n1 * n1) * Math.sqrt(NTOT * n2 - n2 * n2);
//            msg("den:" + den);
            double corelation = 0;
            if (den != 0) {
                corelation = num / den;
            }

            res = termso + "<->" + termta + " =corelation:" + corelation
                    + ", n1:" + resso.result.length
                    + ", n2:" + resta.result.length
                    + ", n12:" + interserct.length;
            System.out.println(res);
            String[][] ex = null;
            if (n12 > 0) {
                //System.out.println(is.getDoc(interserct[0]));
                //System.out.println(is.getDoc(interserct[0]- deltaSOTA));
                ex = new String[(int) Math.min(NB_EXAMPLES, n12)][2];
                for (int i = 0; i < NB_EXAMPLES; i++) {
                    ex[i][0] = is.getDoc(interserct[i]);
                    ex[i][1] = is.getDoc(interserct[0] - deltaSOTA);
                }
            }
            t1.stop();
            return new ItemsCorrelation(termta, (int) n1, (int) n2, (int) n12, (float) corelation, res, ex);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }



    }
}
