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
package org.olanto.mapman.server;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.olanto.idxvli.server.IndexService_MyCat;
import org.olanto.mapman.MapArchiveConstant;
import org.olanto.util.Timer;

/**
 * classe pour l'alignement de bitext.
 *
 * add possibility to insert a CR between paragraph
 *
 */
public class AlignBiText {

    /**
     *
     */
    public SegDoc source;

    /**
     *
     */
    public SegDoc target;

    /**
     *
     */
    public IntMap map;

    /**
     *
     */
    public String query;
    static IndexService_MyCat is;
    static MapService ms;
    static String rootTxt;
    static boolean skipLine;
    static String gloss_name;

    /**
     *
     * @param fileso
     * @param langso
     * @param langta
     * @param query
     * @param w
     * @param h
     * @param remSpace
     */
    public AlignBiText(String fileso, String langso, String langta, String query, int w, int h, Boolean remSpace) {
if (is == null) {
            is = org.olanto.conman.server.GetContentService.getServiceMYCAT("rmi://localhost/VLI");
        }
        if (ms == null) {
            ms = GetMapService.getServiceMAP("rmi://localhost/MAP");
        }
        // initialisisation en cas d'erreur
        System.out.println("file source:" + fileso+" ta:"+langta+" query:"+query);
       if (gloss_name == null){
       try {
            gloss_name = ms.getGLOSS_NAME();
        } catch (RemoteException ex) {
            Logger.getLogger(AlignBiText.class.getName()).log(Level.SEVERE, null, ex);
            gloss_name = "???Glossaries???";
        }
       }
            System.out.println("gloss_name:" + gloss_name);
      if (fileso.toLowerCase().contains("/"+gloss_name.toLowerCase()) && fileso.contains("_" + langso)) {
            fileso = fileso.replace(langso + "/", "XX/");
            System.out.println("Glossaries -> new file source:" + fileso);
        }
        source = new SegDoc(fileso, "file source:" + fileso + " language:" + langso + "\n\n", langso);

        target = new SegDoc("N/A", "file target:" + "N/A" + " language:" + langta + "\n\n", langta);
        map = new IntMap(3);
        source.positions = getLineStat(source.lines, w, h, source.content.length());
        target.positions = getLineStat(target.lines, w, h, target.content.length());


        
        if (rootTxt == null) {
            try {
                rootTxt = is.getROOT_CORPUS_TXT();
            } catch (RemoteException ex) {
                source.lines[1] = "Error:no index server";
                Logger.getLogger(AlignBiText.class.getName()).log(Level.SEVERE, null, ex);
                return; //stop processing
            }
        }
        // tester la langue ....
        // chercher les documents
        fileso = rootTxt + "/" + fileso;
        try {
            skipLine = ms.isSkipLine(); // initialisation from parameter
        } catch (RemoteException ex) {
            Logger.getLogger(AlignBiText.class.getName()).log(Level.SEVERE, null, ex);
        }
        String filepivot = getNameOfDocForThisLang(fileso, "EN");
        String fileta = getNameOfDocForThisLang(fileso, langta);
        //System.out.println("fileso"+fileso+"\n"+"fileta"+fileta+"\n"+"filepivot"+filepivot);
        source = new SegDoc(is, fileso, langso, false);
        if (source.lines[1].startsWith("*** ERROR")) {
            System.out.println("no source file");
            return; //stop processing
        }
        Timer t = new Timer("GetLineStat_Source");
        source.positions = getLineStat(source.lines, w, h, source.content.length());
        t.stop();
        target = new SegDoc(is, fileta, langta, remSpace);
        if (target.lines[1] != null && target.lines[1].startsWith("*** ERROR")) {
            System.out.println("no target file");
            return; //stop processing
        }

        target.positions = getLineStat(target.lines, w, h, target.content.length());
        try {
            // chercher le numéro du doc pivot
            int docpivot = is.getDocId(filepivot);
            if (docpivot != -1) { // existe un docpivot
                //System.out.println(" doc id :"+docso);
                map = ms.getMap(docpivot, langso, langta);
                if (map != null & skipLine) {
                    // map.dump("before skip");
                    map = map.skipLine();  // remap with empty line
                    //map.dump("after skip");
                }
            } else { // pas de docpivot
                map = null;
            }
            if (map == null) {
//                System.out.println("Source size after skip: " + source.lines.length);
//                System.out.println("Target size after skip: " + target.lines.length);
                map = new IntMap(source.lines.length, target.lines.length);
//                map.dump("just constructed");
            }

        } catch (RemoteException ex) {
            System.out.println("source and target files exist but they have no map (build a geometric one!)");
            map = new IntMap(source.lines.length, target.lines.length);
            Logger.getLogger(AlignBiText.class.getName()).log(Level.SEVERE, null, ex);
        }

        // dump();

    }

    /**
     *
     */
    public void dump() {
        source.dump("source");
        target.dump("target");
        map.dump("map");
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
// renommer les variables et ajouter des commentaires dans tout le code
// Matrice (nombre de lignes, position du top, correction, position en pixel)

    /**
     *
     * @param lines
     * @param w
     * @param h
     * @param length
     * @return
     */
    public static int[][] getLineStat(String[] lines, int w, int h, int length) {
        boolean verbose = false;
        int[][] calc = new int[lines.length][5];
        int curLines;

        curLines = getLineNumbers(lines[0], w, " ");
        calc[0][0] = curLines;// nombre de lignes dans la textarea de la phrase courante
        calc[0][1] = 0;// position du curseur pour la phrase courante dans la textarea
        calc[0][2] = 0;// correction pour la position du curseur pour IE afin de mettre au milieu la phrase
        calc[0][3] = 0;// nombre de lignes avant la phrase courante
        int pos = 0, corr, j, ln;
//        if (verbose) {
//           if (verbose) System.out.println("total line numbers: " + lines.length);
//        }
        for (int i = 1; i < lines.length; i++) {
            //ajouter et simuler le comportement du contenu dans un textArea.
            pos += lines[i - 1].length() + 1;
            curLines = getLineNumbers(lines[i], w, " ");

            calc[i][0] = curLines;// nombre de lignes dans la textarea de la phrase courante
            calc[i][1] = pos;
            calc[i][3] = calc[i - 1][0] + calc[i - 1][3]; // nombre de lignes avant la phrase courante
//            if (verbose) {
//                if ((i % 2) == 0) {
//                    System.out.println("line: " + i + " :" + calc[i][0]);
//                }
//            }
        }

        for (int i = 1; i < lines.length; i++) {
            ln = calc[i][0];
            int ln1 = 1;
            corr = calc[i][1];
            j = i + 1;
            // adjust all correction to fit with the middle position
            // for all lines, if the sum of the line numbers is 
            // bigger than the half of the textarea size then put the position on the next line
            while ((j < lines.length) && (ln <= (h / 2) + 1)) {
                ln1 = ln;
                ln += calc[j][0];
                corr = calc[j][1];
                j++;
            }
            // add the correction for the rest of the lines 
            // when the actual last sentence makes more tha one line
            if ((h / 2) > ln1) {
                corr += w * (((h / 2) - ln1) + 1);
            }
            // adjust to the middle of the line
            corr += lines[i].length() / 2 + 1;
            if (corr < length) {
                calc[i][2] = corr;
            } else {
                calc[i][2] = length;
            }
        }
        return calc;
    }

    /**
     *
     * @param line
     * @param taWidth
     * @param Split
     * @return
     */
    public static int getLineNumbers(String line, int taWidth, String Split) {
        boolean verbose = false;
        int curLine = 0;

        // if line length is strictly less than the textarea width it means that
        // the line will stand only on one row in the textarea
        if (line.length() <= taWidth) {
            return 1;
        }
//        System.out.println("start split");
        // Get all the words in the line, check if the line starts already with a character
        int count;
        if (line.startsWith(Split)) {
            count = 1;
        } else {
            count = 0;
        }
        // Split the words of each line
        String[] words = line.split(Split);

        // calculate the sum of the word's lengths one by one if it is still under
        // the textarea width it means it is still in one line
        if (words != null) {
            int i = 0, idx = 0;
            String rest;
//            if (verbose) {
//                System.out.println("start calculating for words length :" + words.length);
//                for (int k = 0; k < words.length; k++) {
//                    System.out.print(words[k] + "|");
//                }
//            }
//            if (verbose) {
//                System.out.println();
//            }

            while (i < words.length) {
//                if (verbose) {
//                    System.out.println("treating word number : " + i + " content : " + words[i] + " position: " + count);
//                }
                if ((words[i].contains("/")) && ((count + words[i].length() + 1) > taWidth)) {
                    if ((words[i].length() + 1) >= taWidth) {
                        curLine++;
                        count = 0;
                    }
                    if ((words[i].contains("-")) && ((count + words[i].length() + 1) >= taWidth)) {
                        rest = words[i];
                        while (((count + rest.length() + 1) > taWidth) && (rest.contains("-"))) {
                            while ((rest.contains("-")) && (count <= taWidth)) {
                                idx = rest.indexOf("-") + 1;
                                count += idx;
                                if ((idx < rest.length() - 1) && (count <= taWidth)) {
                                    rest = rest.substring(idx);
                                } else {
                                    break;
                                }
                            }
                            curLine++;
                            if ((idx >= taWidth) && (taWidth < rest.length() - 1)) {
                                rest = rest.substring(taWidth);
                            } else{
                                rest="";
                            }
                            count = 0;
                        }
                        count = words[i].length() + 1 - words[i].lastIndexOf("-");
//                        if (verbose) {
//                            System.out.println("Case: url with hyphen, new line at word: " + words[i] + " On position: " + count);
//                        }
                    } else {
                        curLine++;
                        count += words[i].length() + 1 - taWidth;
//                        if (verbose) {
//                            System.out.println("Case: url, new line at word: " + words[i] + " On position: " + count);
//                        }
                    }
                    i++;
                } else {
                    // Treat the case of hyphened words, treat them in case of wrapping
                    if ((words[i].contains("-")) && ((count + words[i].length() + 1) > taWidth)) {
                        if ((words[i].length() + 1) >= taWidth) {
                            curLine++;
                            count = 0;
                        }
                        rest = words[i];
                        while (((count + rest.length() + 1) > taWidth) && (rest.contains("-"))) {
                            while ((rest.contains("-")) && (count <= taWidth)) {
                                idx = rest.indexOf("-") + 1;
                                count += idx;
                                if ((idx < rest.length() - 1) && (count <= taWidth)) {
                                    rest = rest.substring(idx);
                                } else {
                                    break;
                                }
                            }
                            curLine++;
                            if ((idx >= taWidth) && (taWidth < rest.length() - 1)) {
                                rest = rest.substring(taWidth);
                            } else{
                                rest="";
                            }
                            count = 0;
                        }
                        count = words[i].length() + 1 - words[i].lastIndexOf("-");
//                        if (verbose) {
//                            System.out.println("Case: url with hyphen, new line at word: " + words[i] + " On position: " + count);
//                        }
                        i++;
                    } else { // Non hyphened words
                        if (i == words.length - 1) {// last word of the line
                            count += words[i].length();
                        } else {// a words in the middle of the line
                            count += words[i].length() + 1;
                        }
                        if ((words[i].startsWith("...."))) {// for these kind of characters the word before is added in front
                            count = words[i].length();
                            if (i > 0) {// if not the first word
                                count += words[i - 1].length();
                            }
                            if (i > 1) {// if the word is not the first then skip a line
                                curLine++;
//                                if (verbose) {
//                                    System.out.println("Case: points, new line at word: " + words[i] + " On position: " + count);
//                                }
                            }
                            while (count >= taWidth) {// compare the number of chars with the textarea width
                                curLine++;
                                count -= taWidth;
//                                if (verbose) {
//                                    System.out.println("Case: too small, new line at word: " + words[i] + " On position: " + count);
//                                }
                            }
                        }
                        if (count < taWidth) {// if the number of chars is still inferior than the width
                            i++;
//                        stay = 0;
                        } else if (count == taWidth) {// if the number is equal then skip and start from next word
                            curLine++;
//                            if (verbose) {
//                                System.out.println("Case: equal, new line at word: " + words[i]);
//                            }
                            count = 0;
                            i++;
                        } else if (count > taWidth) {//  if the number of chars is more than skip a line and then restart conting
                            curLine++;
                            count = words[i].length() + 1;
//                            if (verbose) {
//                                System.out.println("Case: word at the end of the line, new line at word: " + words[i]);
//                            }
                            i++;
                        }
                    }
                }
            }
            if (count >= taWidth) {// if there is any rest askip a line
                while (count >= taWidth) {// compare the number of chars with the textarea width
                    curLine++;
                    count -= taWidth;
//                    if (verbose) {
//                        System.out.println("Case: rest of last word larger than the textarea");
//                    }
                }
            }
            if (count > 0) {// if there is any rest skip a line
                curLine++;
//                if (verbose) {
//                    System.out.println("Case: all the rest, new line at the end");
//                }
            }
//            if (verbose) {
//                System.out.println("end calculating, result : " + curLine);
//            }
        } else {
            curLine = 2;
        }
        return curLine;
    }
}
