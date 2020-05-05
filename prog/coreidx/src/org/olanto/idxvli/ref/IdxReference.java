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
package org.olanto.idxvli.ref;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import static org.olanto.idxvli.ql.QueryOperator.*;

import org.olanto.idxvli.*;
import org.olanto.idxvli.ref.stat.ReferenceStatistic;
import org.olanto.idxvli.server.REFResultNice;
import org.olanto.idxvli.util.SetOfBits;
import org.olanto.util.Timer;
import static org.olanto.idxvli.IdxConstant.*;
import org.olanto.idxvli.ref.stat.InverseRef;

/**
 * Une classe pour référencer un string (pour la version de référence).
 *
 *
 * to do: - l'utilisation de SparseBitSet[] peut etre remplacée par des
 * opérations sur les vecteurs de documents directement
 *
 * modif 2012.4.28 permettre de faire des références sur 3,4,5
 *
 * - modification JG: SetOfBits (copy/reference)
 *
 */
public class IdxReference {

    /**
     * @return the XMLtotword
     */
    public int getXMLtotword() {
        return XMLtotword;
    }

    /**
     * @return the XMLtotwordref
     */
    public int getXMLtotwordref() {
        return XMLtotwordref;
    }

    /**
     * @return the XMLpctref
     */
    public String getXMLpctref() {
        return XMLpctref;
    }

    class ComputeSeqThread extends Thread {

        public final static int THREADFAIL = 1;
        public final static int THREADPASS = 0;
        int _status;
        int id;
        int start;
        int stop;

        public int status() {
            return _status;
        }

        public ComputeSeqThread(int _id, int _stop) {
            _status = THREADFAIL;
            id = _id;
            stop = _stop;
        }

        public void run() {
            boolean verbose = true;
            int count = 0;
            Date start = new Date();
            for (int i = id; i < stop; i += REF_MAX_THREAD) {
                count++;
                if (verbose && count % 2500 == 0) {  // check time  ...
                    Date end = new Date();
                    System.out.println(TaskId() + "Thread " + id + " compute Seq - count:" + count + ":" + (end.getTime() - start.getTime()));
                    start = new Date();
                }

                computeSeq3forN(i);
            }
            //System.out.print("Thread " + id + ": End with success\n");
            System.out.println(TaskId() + " - " + id);
            _status = THREADPASS;
            stop();
            System.out.print(TaskId() + "Error: Thread computeSeq3forN " + id + ": Didn't expect to get here!\n");
            _status = THREADFAIL;
        }
    }

    class ComputeMarkThread extends Thread {

        public final static int THREADFAIL = 1;
        public final static int THREADPASS = 0;
        int _status;
        int id;
        int start;
        int stop;

        public int status() {
            return _status;
        }

        public ComputeMarkThread(int _id, int _stop) {
            _status = THREADFAIL;
            id = _id;
            stop = _stop;
        }

        public void run() {
            boolean verbose = true;
            int count = 0;
            Date start = new Date();
            for (int i = id; i < stop; i += REF_MAX_THREAD) {
                count++;
                if (verbose && count % 2500 == 0) {  // check time  ...
                    Date end = new Date();
                    System.out.println(TaskId() + "Thread " + id + " compute Mark - count:" + count + ":" + (end.getTime() - start.getTime()));
                    start = new Date();
                }

                computeMarkN(i);
            }
            //System.out.print("Thread " + id + ": End with success\n");
            System.out.println(TaskId() + " - " + id);
            _status = THREADPASS;
            stop();
            System.out.print(TaskId() + "Error: Thread ComputeMarkThreadN " + id + ": Didn't expect to get here!\n");
            _status = THREADFAIL;
        }
    }
    public static final int MaxIndexedW = 1000000;
    public static final int NotIndexed = -1;
    private static int globalTaskNumber = 0;
    private int taskNumber;
    static final int MaxMark = 3;
    static final int NoMark = -1;
    private int currentMark = 0;
    private int seqmax = 6;   // borne du seq
    private int seqn = seqmax - 1;   // borne pour les boucles
    public int[] idxW;     // les  mots indexés (numéros)
    public String[] word;  // les mots tokenisés
    public int[] idxpos;   //les positions dans le documents
    private SparseBitSet[] doc;
    public int lastscan;
    public int totwordspacesep;
    protected int lastcp;
    private IdxStructure glue;
    private int[] idxcp;   // index sur le numéro dans le vecteur compacté
    private int[] cpW;     // les  mots indexés (numéros) compacté
    public int[] idxorig;   //l'index dans le vecteur non compacté
    private int[] begM, endM, docM;
    public List<String> docMultiRef;
    public List<String> txtRef;
    public List<String> txtRefOrigin;
    public int nbref;
    private int minlength;
    private SetOfBits sota;  // and of source and target
    private boolean alignsota;
    public int[] posRef;
    private String textforhtml;
    private String[] selectedCollection;
    private String uploadFileName;
    private String collectList;
    private boolean removefirst; // true=remove first reference
    private boolean fast;  // false=remove fantome
    public String removedFile = "no file";
    public int removedDoc = -1;
    private boolean lookforfirst = true;
    private boolean secondpass = false;
    // add to paralellise mark
    private int[] markv;
    private int[] markdocv;
    private int[][] multidocv;
    private String XMLInfo = "<noInfo/>";
    private int XMLtotword;
    private int XMLtotwordref;
    private String XMLpctref;

    private synchronized int GetANewTaskId() {

        globalTaskNumber++;
        return globalTaskNumber;

    }

    private String TaskId() {
        return "Task:" + taskNumber + " ";
    }

    public IdxReference(IdxStructure _glue, String s, int min, String source, String target, boolean alignsota, String[] _selectedCollection,
            boolean removefirst, boolean fast) {
        taskNumber = GetANewTaskId();
        if (removefirst) {
            System.out.println(TaskId() + "IdxReference: find first document");
            IdxReference firstpass = new IdxReference(_glue, s, min, source, target, alignsota, _selectedCollection, false, true);
            String dummyhtml = firstpass.getHTML();
            this.doc = firstpass.doc;  // copy the result
            this.removedFile = firstpass.removedFile;
            this.removedDoc = firstpass.removedDoc;
            lookforfirst = false;
            secondpass = true;

        }
        if (secondpass) {
            System.out.println(TaskId() + "IdxReference: secondpass with removed document");
        }
        InitIdxReference(_glue, s, min, source, target, alignsota, _selectedCollection, removefirst, fast);
    }

    public void InitIdxReference(IdxStructure _glue, String s, int min, String source, String target, boolean alignsota, String[] _selectedCollection,
            boolean _removefirst, boolean _fast) {

        Timer timing = new Timer(TaskId() + "--------------------------------Total reference, size: " + s.length());
        glue = _glue;
        removefirst = _removefirst;
        fast = _fast;
        selectedCollection = _selectedCollection;
        collectList = "";
        System.out.println(TaskId() + "IdxReference: parameters:");
        System.out.println(TaskId() + "   removefirst:" + removefirst);
        System.out.println(TaskId() + "   fast:" + fast);
        System.out.println(TaskId() + "   lookforfirst:" + lookforfirst);
        System.out.println(TaskId() + "   secondpass:" + secondpass);
        if (selectedCollection != null) {
            for (int i = 0; i < selectedCollection.length; i++) {
                collectList += " " + selectedCollection[i].replace("COLLECTION.", "");
            }
        }
        lastscan = 0;
        minlength = min;
        if (minlength < 6) { // alors on utilise le seq3
            seqmax = 3;   // borne du seq
            seqn = seqmax - 1;   // borne pour les boucles
        }
        idxW = new int[MaxIndexedW];
        idxpos = new int[MaxIndexedW];
        word = new String[MaxIndexedW];
        this.alignsota = alignsota;
        if (alignsota) {// filtered by source and target
            sota = new SetOfBits(glue.docstable.satisfyThisProperty("SOURCE." + source)); // une copie pour le premier operande
//           System.out.println("so:"+sota.countTrue());
            SetOfBits ta = glue.docstable.satisfyThisProperty("TARGET." + target);
//            System.out.println("ta:"+ta.countTrue());
            sota.and(ta, SetOfBits.ALL);
//            System.out.println("sota:"+sota.countTrue());
            if (secondpass && removedDoc != -1) {
                System.out.println(TaskId() + "remove first file from filter");
                sota.set(removedDoc, false);

            }
        }
        if (alignsota && selectedCollection != null) {// étend le filtre aux collections           
            SetOfBits colfilter = new SetOfBits(glue.docstable.satisfyThisProperty(selectedCollection[0])); // une copie pour le premier operande
            for (int i = 1; i < selectedCollection.length; i++) {
                SetOfBits col = glue.docstable.satisfyThisProperty(selectedCollection[i]);
                colfilter.or(col, SetOfBits.ALL);
            }
            sota.and(colfilter, SetOfBits.ALL);
        }
        Timer t = new Timer(TaskId() + "Parsing");
        //       textforhtml = s.replace("&", "&amp;").replace("<", "&lt;").replace("\n", "<br/>")+ " EndOfDocument ."; // supprime les return
        textforhtml = "BeginOfDocument <br/>" + s.replace("<", "&lt;").replace("&nbsp;", " ") + " EndOfDocument ."; // &amp sont déjà insérer par le GUI
        textforhtml = addSpace(textforhtml);
        {  // calculer la longueur en mot séparé par des blancs
            String[] words = textforhtml.split("[\\s]");
            totwordspacesep = words.length - 3; // -3  pour ajuster avec les différentes marques ajoutées
        }
        DoParse a = new DoParse(new StringReader(textforhtml), glue.dontIndexThis);
        a.scanString(glue, this);
        t.stop();
        //t = new Timer("Compacting");
        compact();
        //t.stop();
        //t = new Timer("Compute Sequence");
        if (!secondpass) {
            computeSeq3();
        } else {  // already compute in first pass - need to remove first doc
            System.out.println(TaskId() + "instead of computeSeq, removedDoc:" + removedDoc);
            if (removedDoc != -1) {
                for (int i = 0; i < lastscan; i++) {
                    if (doc[i] != null) {
                        doc[i].removebit(removedDoc);
                    } else {
                        doc[i] = new SparseBitSet();
                    }
                }
            }
        }
        //t.stop();
        //t = new Timer("Marking");
        markString();
        //t.stop();
        System.out.println(TaskId() + glue.indexread.getStatistic());
        timing.stop();
        //this.print();
    }

    public void postInit(String fileName) {
        uploadFileName = fileName;
    }

    public static String addSpace(String s) {  // traitement du chinois
        StringBuilder res = new StringBuilder("");
        for (int i = 0; i < s.length(); i++) {
            res.append(s.charAt(i));
            int v = s.charAt(i);
            if ((v >= 0x3000 && v <= 0x9FFF) // unified ideographs +japanese
                    || (v >= 0x3400 && v <= 0x4DFF) // extension A  
                    || (v >= 0x3400 && v <= 0x4DFF) // extension A  
                    || (v >= 0x20000 && v <= 0x2a6df) // extension A  
                    || (v >= 0xf900 && v <= 0xfaff) // compatibility  
                    || (v >= 0x2f800 && v <= 0x2fa1f) // compatibility  
                    ) {
                res.append(" ");
            }
        }
        return res.toString();
    }

    private final void newMark() {
        currentMark++;
        if (currentMark == MaxMark) {
            currentMark = 0;
        }
    }

    private final void compact() {
        int l;
        lastcp = 0;
        idxcp = new int[lastscan];
        idxorig = new int[lastscan];
        cpW = new int[lastscan];
        for (int i = 0; i < lastscan; i++) {
            if (idxW[i] != NotIndexed) {
                idxcp[i] = lastcp;
                cpW[lastcp] = idxW[i];
                idxorig[lastcp] = i;
                lastcp++;
            } else {
                idxcp[i] = NoMark;
            }
        }
        System.out.println(TaskId() + lastscan + " compact " + lastcp);
    }

    private final void computeSeq3() {
        doc = new SparseBitSet[lastscan];
        System.out.print(TaskId() + "Start computeSeq Thread ");
        ComputeSeqThread[] t = new ComputeSeqThread[REF_MAX_THREAD];

        for (int it = 0; it < REF_MAX_THREAD; it++) {
            //System.out.println("Create a thread " + it);
            t[it] = new ComputeSeqThread(it, lastcp - seqn);
            //System.out.println("Start the thread " + it);
            System.out.println(TaskId() + " + " + it);
            t[it].start();
        }
        for (int it = 0; it < REF_MAX_THREAD; it++) {
            //System.out.print("Wait for the thread " + it + " to complete\n");
            try {
                t[it].join();
            } catch (InterruptedException e) {
                System.out.print(TaskId() + "Error: computeSeq Thread " + it + " Join interrupted\n");
            }
        }
        System.out.println(TaskId() + " End Thread ");
    }

    private final void computeSeq3forN(int i) {
        int[] resD;
        if (minlength < 6) { // <6 
            resD = getDocforWseqW3(glue, cpW[i], cpW[i + 1], cpW[i + 2]);
        } else { // >=6
            resD = getDocforWseqW6(glue, cpW[i], cpW[i + 1], cpW[i + 2], cpW[i + 3], cpW[i + 4], cpW[i + 5]);
        }
        //id.showVector(resD);
        if (alignsota) {  // need to be filtered by sota
            int l = resD.length;
            doc[i] = new SparseBitSet();
            for (int j = 0; j < l; j++) {
                int currentDoc = resD[j];
                if (sota.get(currentDoc)) {// check source and target
                    doc[i].insertbit(currentDoc);
                }
            }
        } else { // no filter
            if (resD.length > 0) { // copy result
                doc[i] = new SparseBitSet(resD);
            } else { // empty result
                doc[i] = new SparseBitSet();
            }
        }
    }

    private SparseBitSet getOnlyRealRef(SparseBitSet b, int from, int to) {
        if (from == to || !b.notEmpty()) { // initial ref 3 or 6 is ok
            return b;
        }
        if (REALREF_MAX_CHECK < to + seqmax - from) { // too long
            return b;
        }
        //System.out.print("check: ");
        //Timer t = new Timer("getOnlyRealRef");
        int[] evalseq = new int[to + seqmax - from];
        for (int i = from; i < to + seqmax; i++) {
            evalseq[i - from] = cpW[i];
            //System.out.print(glue.getStringforW(cpW[i])+" ");
        }
        //System.out.print("\n"); 
        int[] resD = getDocforWseqWN(glue, evalseq).doc;
        //System.out.println("check:" + (to + seqmax - from) + ", res size:" + resD.length);

        // transform the result into a sparse ...
        SparseBitSet qres = new SparseBitSet();
        for (int j = 0; j < resD.length; j++) {
            qres.insertbit(resD[j]);
        }
        b = b.and(qres);
        //System.out.println("check after and:" + (to + seqmax - from) + ", res size:" + b.length());
        //t.stop();
        return b;
    }

    private final void computeMarkN(int i) {
        int mark;
        int markdoc = 0;
        int[] multidoc = null;
        SparseBitSet b = doc[i];
        if (b.notEmpty()) { // ok look for the next
            mark = i;

            for (int j = i; j < lastcp - seqn; j++) {
                b = b.and(doc[j]);
                // here we need to check the references
                if (!fast) {
                    b = getOnlyRealRef(b, i, j); // need to be tested before production mode
                }
                if (b.notEmpty()) {
                    mark = j;
                    b.resetcursor();
                    markdoc = b.getNextPos();
                    multidoc = b.getDelta();
                } else {
                    break;
                } // not in the same doc
            }
            markv[i] = mark;
            markdocv[i] = markdoc;
            multidocv[i] = multidoc;
        } else {
            markv[i] = -1;
        }
    }

    private final void computeMark() {
        System.out.println(TaskId() + "Compute marking");
        markv = new int[lastcp - seqn];
        markdocv = new int[lastcp - seqn];
        multidocv = new int[lastcp - seqn][];

        System.out.print(TaskId() + "Start computeMark Thread ");
        ComputeMarkThread[] t = new ComputeMarkThread[REF_MAX_THREAD];

        for (int it = 0; it < REF_MAX_THREAD; it++) {
            //System.out.println("Create a thread " + it);
            t[it] = new ComputeMarkThread(it, lastcp - seqn);
            //System.out.println("Start the thread " + it);
            System.out.println(TaskId() + " + " + it);
            t[it].start();
        }
        for (int it = 0; it < REF_MAX_THREAD; it++) {
            //System.out.print("Wait for the thread " + it + " to complete\n");
            try {
                t[it].join();
            } catch (InterruptedException e) {
                System.out.print(TaskId() + "Error: computeMark Thread  " + it + " Join interrupted\n");
            }
        }
        System.out.println(TaskId() + " End Thread ");

    }

    private final void markString() {

        begM = new int[lastscan];
        for (int i = 0; i < lastscan; i++) {
            begM[i] = NoMark;
        }
        endM = new int[lastscan + 1]; // for the last !
        for (int i = 0; i < lastscan; i++) {
            endM[i] = NoMark;
        }
        docM = new int[lastscan];
        docMultiRef = new Vector<String>();
        txtRef = new Vector<String>();
        txtRefOrigin = new Vector<String>();
        int maxmarked = -1;
        int mark;
        int markdoc = 0;
        int[] multidoc = null;
        if ((lastcp - seqn) > 0) {
            computeMark(); // not to short
        }
        for (int i = 0; i < lastcp - seqn; i++) {
            if (markv[i] != -1) { // ok look for the next
                mark = markv[i];  //reload current value
                markdoc = markdocv[i];
                multidoc = multidocv[i];
                if ((mark > maxmarked) && ((mark - i) >= minlength - seqmax)) { // not included in a bigger ref
                    maxmarked = mark; // new max
                    newMark();
                    begM[i] = currentMark;
                    endM[maxmarked + seqmax] = currentMark;
                    docM[i] = markdoc; // get the first ref (if many)
                    nbref++;
                    /*
                     String dlist = glue.getFileNameForDocument(multidoc[0]); // liste des références
                     for (int k = 1; k < multidoc.length; k++) {
                     dlist += REFResultNice.DOC_REF_SEPARATOR + glue.getFileNameForDocument(multidoc[k]);
                     }
                     docMultiRef.add(dlist);
                     String tlist = glue.getStringforW(cpW[i]); // texte des références
                     for (int k = i + 1; k < maxmarked + seqmax; k++) {
                     tlist += " " + glue.getStringforW(cpW[k]);
                     }
                     */
                    //Timer t1 = new Timer("collect fname");
                    StringBuilder dlist = new StringBuilder(glue.getFileNameForDocument(multidoc[0])); // liste des références
                    for (int k = 1; k < multidoc.length; k++) {
                        dlist.append(REFResultNice.DOC_REF_SEPARATOR).append(glue.getFileNameForDocument(multidoc[k]));
                    }
                    docMultiRef.add(dlist.toString());
                    //t1.stop();
                    //Timer t2 = new Timer("collect word");
                    StringBuilder tlist = new StringBuilder(glue.getStringforW(cpW[i])); // texte des références
                    for (int k = i + 1; k < maxmarked + seqmax; k++) {
                        tlist.append(" ").append(glue.getStringforW(cpW[k]));
                    }

                    txtRef.add(tlist.toString());
                    //t2.stop();
                    //Timer t3 = new Timer("add noref part");
                    txtRefOrigin.add(
                            textforhtml.substring(idxpos[idxorig[i]] - word[idxorig[i]].length() - 1,
                            idxpos[idxorig[maxmarked + seqmax - 1]] - 1));
                    //t3.stop();
                }
            }// if
            //t0.stop();
        }// for
        System.out.println(TaskId() + "nbref: " + nbref);
//        for (int i = 0; i < nbref; i++) {
//            System.out.println(i + " -------------");
//            System.out.println("doc:" + docMultiRef.get(i));
//            System.out.println("origin:" + txtRefOrigin.get(i));
//            System.out.println("indexed:" + txtRef.get(i));
//        }

    }

    public final String getXMLInfo() {
        return XMLInfo;
    }

    public final String getHTML() {

        StringBuilder s = new StringBuilder("");
        s.append("<html>\n");
        s.append("<head>\n");
        s.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">\n");
        s.append("<title>myQuote</title>");
        s.append("</head>\n");
        s.append("<body>\n");
        if (MYQUOTE_STAT) {
            s.append("<A NAME=\"TOP\">" + "</A>"
                    + "<A HREF=\"#STATISTIC\">" + MSG.get("server.qd.MSG_19") + "</A>"
                    + "<br/>");
        }
        s.append(getXML());
        if (MYQUOTE_STAT) {
            s.append("<A NAME=\"STATISTIC\">" + "</A>"
                    + "<A HREF=\"#TOP\">" + MSG.get("server.qd.MSG_18") + "</A>"
                    + "<br/>");
            s.append(getSTAT());
            s.append(getREF());
        }
        s.append("</body>\n");
        s.append("</html>\n");
        return s.toString();
    }

    public final String getREF() {
        //Timer timing = new Timer("--------------------------------Total getXML");
        StringBuilder s = new StringBuilder("");
        // s.append("<P>\nHere the references to be saved\n");

        s.append("\n<!--MYQUOTEREF");
        s.append("\n" + nbref);
        for (int i = 0; i < nbref; i++) {
            s.append("\n" + i + REFResultNice.DOC_REF_SEPARATOR + txtRef.get(i) + REFResultNice.DOC_REF_SEPARATOR + docMultiRef.get(i));
        }
        s.append("\nMYQUOTEREF-->");

        s.append("</P>\n");
        //timing.stop();
        return s.toString();
    }

    public final String getSTAT() {
        //Timer timing = new Timer("--------------------------------Total getXML");

        StringBuilder s = new StringBuilder("<P>\n");
        s.append("<hr/>\n");
        //
        if (lookforfirst) {
            System.out.println(TaskId() + "try to get first document");
            NumberFormat formatter = new DecimalFormat("#0.0");
            ReferenceStatistic firstpass = new ReferenceStatistic(txtRefOrigin, docMultiRef, totwordspacesep, removefirst, fast, removedFile);
            InverseRef firstref = firstpass.getFirsReference();
            if (!(firstref == null)) {
                removedDoc = glue.getIntForDocument(firstref.docref);
                removedFile = firstref.docref + " (" + formatter.format(firstref.pcttotword) + "%)";
            }
        }
        ReferenceStatistic rs = new ReferenceStatistic(txtRefOrigin, docMultiRef, totwordspacesep, removefirst, fast, removedFile);
        s.append(rs.getHeaderSat(uploadFileName, collectList, minlength));
        s.append("<hr/>\n");
        s.append(rs.getStatByRef());
        s.append("<hr/>\n");
        s.append(rs.getStatByQuote());
        s.append("<hr/>\n");

        XMLInfo = getXMLInfo(rs);
        XMLtotword = rs.totword;
        XMLtotwordref = rs.totwordref;
        XMLpctref = rs.pctref;
        //timing.stop();
        return s.toString();
    }

    public final String getXMLInfo(ReferenceStatistic rs) {
        StringBuilder s = new StringBuilder("<Info>\n");
        s.append(rs.getXMLStatByQuote());
        s.append("</Info>\n");

        return s.toString();
    }

    public final String getXML() {
        //Timer timing = new Timer("--------------------------------Total getXML");
        StringBuilder s = new StringBuilder("<P>\n");
        posRef = new int[nbref];
        int countchar = 1;
        int countrefstart = 0;
        int countrefstop = 0;
        int openRef = 0;  // pour gérer les intersections de références
        String seqofstopword = "";
        for (int i = 0; i < lastscan - 1; i++) {
//            System.out.println("index:"+(i+1)+"="+idxcp[i + 1]);
            if (idxcp[i + 1] != NoMark) {
//                System.out.println("index:" + (i + 1) + "=" + idxcp[i + 1] + ", " + begM[idxcp[i + 1]] + ", " + endM[idxcp[i + 1]]);
                if (endM[idxcp[i + 1]] != NoMark) {
//                    System.out.println("before endM:" + s.toString());
//                    System.out.println("before endM stopword:" + seqofstopword);
                    openRef--;
                    countrefstop++;
                    String targetTxt = CLOSE_REF_BEG + countrefstop + CLOSE_REF_END;
                    countchar += targetTxt.length();
                    countchar += seqofstopword.length();           
                    if (openRef >= 1) {
                        s.append(targetTxt).append(seqofstopword);
                    } else {
                        s.append(targetTxt).append("</a>").append(seqofstopword);
                    }
                    seqofstopword = "";
                } // if
                if (begM[idxcp[i + 1]] != NoMark) {
                    openRef++;
                    posRef[countrefstart] = countchar;
                    countrefstart++;
                    String targetTxt = OPEN_REF_BEG + countrefstart + OPEN_REF_END;
                    countchar += targetTxt.length();
                    countchar += seqofstopword.length();  
                    if (openRef > 1) {
                        s.append("</a>").append(seqofstopword);
                        seqofstopword = "";
                    }
                    s.append(seqofstopword).append("<a href=\"#").append(countrefstart).append("\" id=\"ref").append(countrefstart).append("\" onClick=\"return gwtnav(this);\">").append(targetTxt);
                 seqofstopword = "";
                } //if
            } //if
            String addtohtml;
            if (i < lastscan - 1) {
                if (idxcp[i + 1] != NoMark) { // indexed word                
                    addtohtml = seqofstopword+textforhtml.substring(idxpos[i], idxpos[i + 1]);
                    seqofstopword = "";
                } else { // stop word
                    seqofstopword += textforhtml.substring(idxpos[i], idxpos[i + 1]);
                    addtohtml="";
                }
                
            } else {
                addtohtml = textforhtml.substring(idxpos[i], textforhtml.length());
            }
            countchar += addtohtml.length();
            s.append(addtohtml);
        } // for
//        if (endM[idxcp[lastscan - 1] + 1] != NoMark) { // si la référence va jusqu'à la fin
        if (openRef > 0) { // si la référence va jusqu'à la fin
            countrefstop++;
            String targetTxt = CLOSE_REF_BEG + countrefstop + CLOSE_REF_END;
            countchar += targetTxt.length();
            s.append(targetTxt).append("</a>");
        } // if
        s.append("</P>\n");
        //timing.stop();
        if (SKIP_LINE_QUOTE_DECTECTOR) {
            return s.toString().replace("\n", "<br/><br/>");
        }
        return s.toString().replace("\n", "<br/>");
    }

    private final void print() {
        System.out.println(TaskId() + "Scanned String lenght" + lastscan);
        for (int i = 0; i < lastscan - 1; i++) {
            System.out.println(TaskId() + i + "/" + word[i] + "/" + idxpos[i] + "/" + idxW[i] + "/" + idxcp[i] + "/" + begM[i] + "/" + endM[i] + "/" + docM[i]);
            doc[i].print();
        }
    }
}
