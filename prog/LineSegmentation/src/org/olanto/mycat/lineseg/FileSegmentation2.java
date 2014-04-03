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
package org.olanto.mycat.lineseg;

import com.ibm.icu.text.BreakIterator;
//import java.text.BreakIterator;
import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import com.ibm.icu.util.ULocale;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import java.util.List;
//import java.util.Locale;
import java.util.Vector;
import org.olanto.senseos.SenseOS;
import org.olanto.util.StringManipulation;

/**
 * pour segmenter un fichier txt en phrases.
 */
public class FileSegmentation2 {

    static final int verySmallSentence = 15;
    static BreakIterator boundary;
    //static List<String> abreviation = new Vector<String>();
    static String language;
    static SetOfReplacements replace;
    static SetOfAbreviations abrev;
    static StringManipulation stringManip = new StringManipulation();

    public static void init(String _language) {
        if (replace == null) {
            replace = new SetOfReplacements(SegmentationConstant.FILE_REPLACE);
        }
        if (_language.equals("???")) {
            System.out.println("default=EN");
            _language = "ENGLISH";
        }
        language = _language;
        String dictionnary = SenseOS.getMYCAT_HOME() + "/config/dict";
        //readAbreviation(dictionnary + "/" + language + ".txt");
        abrev = new SetOfAbreviations(dictionnary + "/" + language + ".txt");
        System.out.println("abbréviation:" + dictionnary);

        if (_language.equals("FRENCH")) {
            boundary = BreakIterator.getSentenceInstance(ULocale.FRENCH);
        }
        if (_language.equals("ENGLISH")) {
            boundary = BreakIterator.getSentenceInstance(ULocale.ENGLISH);
        }
        if (_language.equals("RUSSIAN")) {
            boundary = BreakIterator.getSentenceInstance(new ULocale("ru"));
        }
        if (_language.equals("ARABIC")) {
            boundary = BreakIterator.getSentenceInstance(new ULocale("ar"));
        }
        if (_language.equals("CHINESE")) {
            boundary = BreakIterator.getSentenceInstance(ULocale.CHINESE);
        }
        if (_language.equals("SPANISH")) {
            boundary = BreakIterator.getSentenceInstance(new ULocale("es"));
        }
        if (_language.equals("PORTUGUESE")) {
            boundary = BreakIterator.getSentenceInstance(new ULocale("pt"));
        }
    }

    public static long getLastModified(String path) {
        long lastModified = (new File(path)).lastModified();
        return lastModified;
    }

    public static void setLastModified(String path, long lastModified) {
        (new File(path)).setLastModified(lastModified);
    }

    public static List<String> readFile(String path, String inputEncoding, boolean autodetect) {
        List<String> res = new Vector<String>();
        try {
            BufferedReader in;

            if (autodetect) {
                CharsetDetector detector = new CharsetDetector();
                CharsetMatch match;
                FileInputStream stream = new FileInputStream(path);
                BufferedInputStream streamData = new BufferedInputStream(stream);
                detector.setText(streamData);
                match = detector.detect();
//                System.out.println(match.getLanguage() + "-" + match.getName() + "-" + match.getConfidence());
                in = new BufferedReader(new InputStreamReader(new FileInputStream(path), match.getName()));
            } else {
                in = new BufferedReader(new InputStreamReader(new FileInputStream(path), inputEncoding));
            }


             String w;
            w = in.readLine();

            while (w != null) {
                //                   res.append(w + "\n");
//                System.out.println("-----------------------------------------");
//                System.out.println("raw :"+w);
                w = cleanSpaceAndTab(w);
//                 System.out.println("clean1 :"+w);
                w = " " + w;
//                w = preAbbr(w);
//               System.out.println("clean2 :"+w);


                List<String> ls = getSentences(w, false);
                List<String> ls2 = postSeg(ls);
                for (String s : ls2) {
                    s = s.trim();
                    if (!s.equals("")) {
                        if (language.equals("CHINESE")) {
                            //System.out.println("add-space");
                            s = stringManip.addSpace(s);
                        }
                        res.add(s);
                    }
                }

                w = in.readLine();

            }
 
            in.close();
            in = null;

        } catch (Exception e) {
            System.err.println("IO error during read :");
            e.printStackTrace();
        }
        return res;
    }

   
    public static String cleanSpaceAndTab(String s) {
//        System.out.println("-------cst:"+s);
//        for (int i=0; i<s.length();i++){
//            int v=s.charAt(i);
//             System.out.println(i+":"+s.substring(i, i+1)+":"+v);
//        }
        replace.add("\t", " ");
        char x20 = 0x20;
        char xa0 = 0xa0;
//        System.out.println("nbsp")
//            System.out.println("nbsp:" + s);
        replace.add("" + xa0 + x20, " ");
//        System.out.println("1e");
        char x1e = 0x1e;
        replace.add("" + x1e, " ");
//        System.out.println("1f");
        char x1f = 0x1f;
        replace.add("" + x1f, "");
//         System.out.println("02");
        char x02 = 0x02;
        replace.add("" + x02, " ");
//          System.out.println("13");
        char x13 = 0x13;
        replace.add("" + x13, " ");
//          System.out.println("15");
        char x15 = 0x15;
        replace.add("" + x15, " ");
//          System.out.println("00");
        char x00 = 0x00;
        replace.add("" + x00, " ");
//          System.out.println("0b");
        char x0b = 0x0b;
        replace.add("" + x0b, " ");
//          System.out.println("0c");
        char x0c = 0x0c;
        replace.add("" + x0c, " ");
//          System.out.println("double blanc");
        s = replace.replaceAll2(s); // replace from list
        s = replace.replace(s,"  ", " ");
//            System.out.println("return");
        return s.trim();
    }

 
    public static List<String> postSeg(List<String> source) {
        Vector<String> res = new Vector<String>();
        if (source.size() <= 1) {
            String s = source.get(0);
          //   s = postAbbr(s);
            res.add(s);
            return res;
        }
        boolean pasteWithNext = false;
        int count = 1;
        for (String s : source) {
            //  s = postAbbr(s);
            if (pasteWithNext) {
                String paste = res.lastElement()+ " " + s;
                paste = replace.replace(paste,"  ", " ");
                res.setElementAt(paste , res.size() - 1);
                pasteWithNext = false;
            } else {
                res.add(s);
                count++;
            }
            if (s.length() < verySmallSentence) {
                //System.out.println(count + "-" + s);
                pasteWithNext = true;
            }
           if (abrev.endWithAbr(s)) {
                //System.out.println(count + "-" + s);
                pasteWithNext = true;
            }
            //System.out.println(count + "-" + res.lastElement());

        }

        return res;
    }

// Print each element in order
    public static List<String> getSentences(String source, boolean verbose) {
        Vector<String> res = new Vector<String>();
        boundary.setText(source);
        int start = boundary.first();
        int count = 1;
        for (int end = boundary.next();
                end != BreakIterator.DONE;
                start = end, end = boundary.next()) {
            if (verbose) {
                System.out.println(count + "-" + source.substring(start, end));
            }
            res.add(source.substring(start, end));
            count++;
        }
        return res;
    }
}
