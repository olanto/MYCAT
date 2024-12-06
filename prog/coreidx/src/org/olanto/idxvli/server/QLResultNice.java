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
package org.olanto.idxvli.server;

import org.olanto.util.Timer;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.olanto.idxvli.*;
import org.olanto.idxvli.extra.*;
import org.olanto.conman.server.*;
import org.olanto.idxvli.ql.OrderByDate;
import org.olanto.idxvli.ql.OrderByName;
import org.olanto.idxvli.ql.OrderResult;

/**
 * Classe stockant les résultats d'une requête avec titre et snipet.
 *
 * <p>
 *
 */
public class QLResultNice implements Serializable, Cloneable {

    /* les documents résultats */

    /**
     *
     */

    public int[] result;
    /* les partie du results */

    /**
     *
     */

    public String[] docname;
    private long[] docdate;  // initialisé seulement en cas de tri ...

    /**
     *
     */
    public String[] title;

    /**
     *
     */
    public String[] clue;
    /* la durée d'exécution */

    /**
     *
     */

    public long duration;  // en ms
    /* autour de la requête */

    /**
     *
     */

    public String[] termsOfQuery;

    /**
     *
     */
    public String query;

    /**
     *
     */
    public String query2;

    /**
     *
     */
    public String properties;

    /**
     *
     */
    public String profile;

    /**
     *
     */
    public String alternative;
    /*  exact filtering */
    private long durationExactFiltering;  // en ms
    private int lastexact;
    private int lastjoin;
    private int countcheckfile;
    private boolean[] selected;
    private boolean exactDone;

    /**
     * crée un résultat
     *
     */
    @Override
    public QLResultNice clone() {
        try {
            return (QLResultNice) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(QLResultNice.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * @param query1
     * @param query2
     * @param properties
     * @param profile
     * @param termsOfQuery
     * @param result
     * @param docname
     * @param title
     * @param clue
     * @param duration
     * @param alternative
     */
    public QLResultNice(String query1, String query2, String properties, String profile, String[] termsOfQuery, int[] result, String[] docname, String[] title, String[] clue, long duration, String alternative) {
        this.query2 = query2;
        this.query = query1;
        this.properties = properties;
        this.profile = profile;
        this.termsOfQuery = termsOfQuery;
        this.result = result;
        this.docname = docname;
        this.title = title;
        this.clue = clue;
        this.duration = duration;
        this.alternative = alternative;
    }

    /**
     * crée un résultat
     *
     * @param query
     * @param result id des documents
     * @param properties
     * @param profile
     * @param duration dur�e
     * @param docname
     * @param termsOfQuery
     * @param clue
     * @param title
     * @param alternative
     */
    public QLResultNice(String query, String properties, String profile, String[] termsOfQuery, int[] result, String[] docname, String[] title, String[] clue, long duration, String alternative) {
        this.query = query;
        this.properties = properties;
        this.profile = profile;
        this.termsOfQuery = termsOfQuery;
        this.result = result;
        this.docname = docname;
        this.title = title;
        this.clue = clue;
        this.duration = duration;
        this.alternative = alternative;
    }

    /**
     *
     * @param s
     */
    public void dump(String s) {
        System.out.println("--- dump QLResult at :" + s);
        for (int i = 0; i < result.length; i++) {
            System.out.println(i + " - " + result[i] + " - " + docname[i]);
        }
    }

    /**
     *
     * @param entry
     */
    public void hilite(int entry) {
        for (int i = 0; i < termsOfQuery.length; i++) {
            //     docname[entry]=showTerm(docname[entry],voc[i]);
            title[entry] = showTerm(title[entry], termsOfQuery[i]);
            clue[entry] = showTerm(clue[entry], termsOfQuery[i]);
        }
    }

    /**
     *
     * @param s
     * @param term
     * @return
     */
    public static String showTerm(String s, String term) {
        if (s != null && term.length() > 2) // au moins trois caract�res
        {
            return s.replace(term, "<b>" + term + "</b>");
        }
        return s;
    }

    /**
     *
     * @param tobejoined
     */
    public void fusionResult(int[] tobejoined) {
        boolean verbose = true;
        if (verbose) {
            System.out.println("fusion two request");
        }
        if (result.length == 0 || tobejoined.length == 0) {  // no result
            if (verbose) {
                System.out.println("empty join");
            }
            return;
        }

        selected = new boolean[result.length];
        lastjoin = 0;
        int lastJ = 0;
        for (int i = 0; i < result.length; i++) {
            for (int j = lastJ; j < tobejoined.length; j++) {
                if (result[i] == tobejoined[j]) {
//                    System.out.println(i + "," + j + " - " + result[i] + "=" + tobejoined[j]);
                    selected[i] = true; // mark ok
                    lastjoin++;
                    lastJ = j;
                    break;
                }
                if (result[i] < tobejoined[j]) {
//                    System.out.println(i + "," + j + " - " + result[i] + "<" + tobejoined[j]);
                    lastJ = j;
                    break;
                }
//                System.out.println(i + "," + j + " - " + result[i] + ">" + tobejoined[j]);
            }
        }
        // compress result
        int[] newResult = new int[lastjoin];
        String[] newDocname = new String[lastjoin];
        String[] newTitle = new String[lastjoin];
        String[] newClue = new String[lastjoin];
        if (lastjoin != 0) {// exist exact to be copied
            int current = 0;
            for (int i = 0; i < result.length; i++) {
                if (selected[i]) {
                    // System.out.println("i: " + i+", current: " + current);
                    newResult[current] = result[i];
                    newDocname[current] = docname[i];
                    newTitle[current] = title[i];
                    newClue[current] = clue[i];
                    current++;
                }
            }
        }
        // replace 
        result = newResult;
        docname = newDocname;
        title = newTitle;
        clue = newClue;



    }

    /**
     *
     * @param id
     * @param size
     * @param chardist
     */
    public void checkIfRealyNear(IdxStructure id, int size, int chardist) {

        String close1 = getExactExp(query);
        String close2 = getExactExp(query2);
        countcheckfile = 0;
        lastexact = 0;
        selected = new boolean[result.length];
        for (int i = 0; i < result.length; i++) {
            countcheckfile++;
            List<Integer> idx1 = id.idxOfExpInDoc(close1, result[i], docname[i]);
//            System.out.print("close1=");
//            for (int j = 0; j < idx1.size(); j++) {
//                System.out.print(idx1.get(j) + " ");
//            }
//            System.out.print("\n");
            List<Integer> idx2 = id.idxOfExpInDoc(close2, result[i], docname[i]);
//            System.out.print("close2=");
//            for (int j = 0; j < idx2.size(); j++) {
//                System.out.print(idx2.get(j) + " ");
//            }
//            System.out.print("\n");
            // check close validity
            boolean nearOK = false;
            for (int j = 0; j < idx1.size(); j++) {
                for (int k = 0; k < idx2.size(); k++) {
                    if ((idx2.get(k) > idx1.get(j) && idx2.get(k) - (idx1.get(j) + close1.length()) < chardist)
                            || (idx1.get(j) > idx2.get(k) && idx1.get(j) - (idx2.get(k) + close2.length()) < chardist)) {
                        selected[i] = true;
                        lastexact++;
                        nearOK = true;
                        break;
                    }

                }
                if (nearOK) {
                    break;
                }
            }
            if (lastexact == size) {// enough results
                break;
            }
        }
        // compress result
//        System.out.println("lastExact" + lastexact);
        int[] exactResult = new int[lastexact];
        String[] exactDocname = new String[lastexact];
        String[] exactTitle = new String[lastexact];
        String[] exactClue = new String[lastexact];
        if (lastexact != 0) {// exist exact to be copied
            int current = 0;
            for (int i = 0; i < countcheckfile; i++) {
                if (selected[i]) {
                    // System.out.println("i: " + i+", current: " + current);
                    exactResult[current] = result[i];
                    exactDocname[current] = docname[i];
                    exactTitle[current] = title[i];
                    exactClue[current] = clue[i];
                    current++;
                }

            }
        }
        // replace  by close result
        result = exactResult;
        docname = exactDocname;
        title = exactTitle;
        clue = exactClue;

        // finish
    }

    /**
     *
     * @param id
     * @param size
     * @param close2
     */
    public void checkExactClose(IdxStructure id, int size, String close2) {
        query2 = close2;
        checkExactInternal(id, Integer.MAX_VALUE, query);  // first expression
        checkExactInternal(id, Integer.MAX_VALUE, query2);  // second expression
    }

    /**
     *
     * @param id
     * @param size
     */
    public void checkExact(IdxStructure id, int size) {
        if (!exactDone) { // dont do this many times !!
            exactDone = true;
            checkExactInternal(id, size, query);
        }
    }

    private void checkExactInternal(IdxStructure id, int size, String exactexp) {

        boolean verbose = true;
        Timer time = new Timer(exactexp, true);
        if (verbose) {
            System.out.println("check exact expression");
        }
        if (result.length == 0) {  // no result
            if (verbose) {
                System.out.println("check exact expression");
            }
            return;
        }
        String exactExpression = getExactExp(exactexp);
        if (exactExpression == null) {  // no result
            System.out.println("Error: Query is not QUOTATION(\"....\") no filtering for exact matching: " + exactexp);
            return;
        }
        if (verbose) {
            System.out.println("Exact expression is \"" + exactExpression + "\"");
        }
        selected = new boolean[result.length];
        lastexact = 0;
        countcheckfile = 0;
        for (int i = 0; i < result.length; i++) {
            countcheckfile++;
            if (id.isExactExpInDoc(exactExpression, result[i], docname[i])) {
//                System.out.println("\"" + exactExpression + "\" is in " + docname[i]);
                selected[i] = true; // mark ok
                lastexact++;
            } else {
//                System.out.println(exactExpression + "\" is not in " + docname[i]);
            }
            if (lastexact == size) {// enough results
                break;
            }
        }

        // compress result
//        System.out.println("lastExact" + lastexact);
        int[] exactResult = new int[lastexact];
        String[] exactDocname = new String[lastexact];
        String[] exactTitle = new String[lastexact];
        String[] exactClue = new String[lastexact];
        if (lastexact != 0) {// exist exact to be copied
            int current = 0;
            for (int i = 0; i < countcheckfile; i++) {
                if (selected[i]) {
                    // System.out.println("i: " + i+", current: " + current);
                    exactResult[current] = result[i];
                    exactDocname[current] = docname[i];
                    exactTitle[current] = title[i];
                    exactClue[current] = clue[i];
                    current++;
                }

            }
        }
        // replace fuzzy result by exact result
        result = exactResult;
        docname = exactDocname;
        title = exactTitle;
        clue = exactClue;

        // finish

        durationExactFiltering = time.getstop();
        if (verbose) {
            System.out.println("duration of check exact expression: " + durationExactFiltering + " (ms)"
                    + " #checkfile: " + countcheckfile + " #exact: " + lastexact + " for:\"" + exactExpression + "\"");
        }


    }

    private String getExactExp(String exactexp) {

        int beg = exactexp.indexOf("QUOTATION(\"");
        int end = exactexp.indexOf("\")");
        if (end == -1 || beg == -1) {  // no result
            System.out.println("Error: Query is not QUOTATION(\"....\") no filtering for exact matching");
            return null;
        }
        return exactexp.substring(beg + 11, end);
    }

    /**
     *
     * @param id
     * @param kind
     */
    public void orderBy(IdxStructure id, String kind) {
//        System.out.println("orderBy: " + kind);
        if (kind.equals("DATE")) { // by date
            docdate = new long[result.length];
            for (int i = 0; i < result.length; i++) { // charge les noms pour le tri
                if (docname[i] == null) { // par encore évalué
                    docname[i] = id.getFileNameForDocument(result[i]);
                }
                docdate[i] = id.getDateOfD(result[i]);
            }
            OrderResult[] allres = new OrderResult[result.length]; // constuit un vecteur de doc
            for (int i = 0; i < result.length; i++) {
                allres[i] = new OrderResult(result[i], docname[i], docdate[i]);
            }

            Arrays.sort(allres, new OrderByDate());
            for (int i = 0; i < result.length; i++) { // réécrit le résultat
                result[i] = allres[i].id;
                docname[i] = allres[i].name;
                docdate[i] = allres[i].date;
                //System.out.println("orderByDate: " + result[i] + " / " + docdate[i] + " / " + docname[i]);
            }

            return;
        }
        if (kind.equals("NAME")) {// by name
            for (int i = 0; i < result.length; i++) { // charge les noms pour le tri
                if (docname[i] == null) { // par encore évalué
                    docname[i] = id.getFileNameForDocument(result[i]);
                }
            }
            OrderResult[] allres = new OrderResult[result.length]; // constuit un vecteur de doc
            for (int i = 0; i < result.length; i++) {
                allres[i] = new OrderResult(result[i], docname[i], 0);
            }

            Arrays.sort(allres, new OrderByName());
            for (int i = 0; i < result.length; i++) { // réécrit le résultat
                result[i] = allres[i].id;
                docname[i] = allres[i].name;
                //System.out.println("orderByName: " + result[i] + " / " + docname[i]);
            }
            return;
        }
        // by ranking nothing to do
        for (int i = 0; i < result.length; i++) {
            //System.out.println("orderByRanking: " + result[i] + " / " + docname[i]);
        }
    }

    /**
     *
     * @param id
     * @param cs
     * @param request
     * @param start
     * @param size
     * @param fullresult
     */
    public void update(IdxStructure id, ContentService cs, String request, int start, int size, boolean fullresult) { // pour le search engine
        if (!fullresult) {
            boolean contentservice = cs != null;
            Timer time = new Timer(request, true);
            if (result == null) {// rien � faire
                return;
            } else { // il a des r�sultats
                //msg("nb res:"+result.length);
                for (int i = Math.max(0, start); i < Math.min(start + size, result.length); i++) {
                    if (docname[i] == null) { // par encore �valu�
                        String currentRef = id.getFileNameForDocument(result[i]);
                        if (contentservice) {
                            try {
                                docname[i] = cs.getRefName(currentRef);
                                title[i] = cs.getTitle(currentRef);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        clue[i] = "";
                        for (int j = 0; j < termsOfQuery.length; j++) {
                            if (termsOfQuery[j].length() > 2) { // marque pas les trop petits
                                // on doit aussi �liminer les termes des la requ�te AND .... � faire !!!!!!!!!!!!!!!!!!!!!
                                FromTo fromto = DocPosChar.extractIntervalForW(result[i], id, termsOfQuery[j], 4);
                                if (fromto != null) {
                                    if (contentservice) {
                                        try {
                                            clue[i] += cs.getCleanText(currentRef, fromto.from, fromto.to) + "...";
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    hilite(i);
                    this.duration = time.getstop(); //update time
                }
            }
        }
    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        out.writeObject(result);
        out.writeObject(docname);
        out.writeObject(title);
        out.writeObject(clue);
        out.writeLong(duration);
        out.writeObject(termsOfQuery);
        out.writeObject(query);
        out.writeObject(alternative);
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        result = (int[]) in.readObject();
        docname = (String[]) in.readObject();
        title = (String[]) in.readObject();
        clue = (String[]) in.readObject();
        duration = in.readLong();
        termsOfQuery = (String[]) in.readObject();
        query = (String) in.readObject();
        alternative = (String) in.readObject();
    }
}
