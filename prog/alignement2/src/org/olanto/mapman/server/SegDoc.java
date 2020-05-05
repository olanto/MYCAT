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

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.olanto.idxvli.server.IndexService_MyCat;
import static org.olanto.util.Messages.*;
import org.olanto.util.StringManipulation;

/**
 * Une classe pour stocker un document sous forme de phrase.
 *
 *
 */
public class SegDoc {

    public String[] lines;
    public int[][] positions;
    public int nblines;
    public String content;
    public String uri;
    public String lang;
    public String txt_encoding = "UTF-8";
    static StringManipulation stringManip = new StringManipulation();

    public void dump(String s) {
        System.out.println("--------------------------------------------");
        System.out.println(s);
        System.out.println("nblines:" + nblines);
        System.out.println("lines.length:" + lines.length);
        System.out.println("positions.lenght:" + positions.length);
        for (int i = 0; i < lines.length; i++) {
            System.out.println("line " + i + ":" + lines[i]);
        }
        for (int i = 0; i < positions.length; i++) {
            System.out.print("positions " + i + ":");
            for (int j = 0; j < positions[i].length; j++) {
                System.out.print(positions[i][j] + ", ");
            }
            System.out.println();
        }
    }

    public SegDoc(IndexService_MyCat is, String fname, String lang, boolean remSpace) {
        uri = fname;
        this.lang = lang;
        try {
            System.out.println("get file:" + fname +" remove space:"+ true);
            content = is.getDoc(is.getDocId(fname));
            if (remSpace) {
                content = stringManip.removeSpace(content);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(SegDoc.class.getName()).log(Level.SEVERE, null, ex);
            content = null;
        }
        normalizeContent();
    }

//    public SegDoc(String fname, String lang) {
//        uri = fname;
//        this.lang = lang;
////        System.out.println("Building from segdoc from file:" + fname);
//        content = file2String(fname, txt_encoding);
//        normalizeContent();
//    }
    private void normalizeContent() {
        if ((content != null) && (AlignBiText.skipLine)) {
            //msg("skipline");
            content = content.replace("\n", "\n\n");
        }
        if (content == null) {
            content = "file source:" + uri + " language:" + lang + "\n*** ERROR :no file\n";
        }
        if (content.length() == 0) {
            content = "file source:" + uri + " language:" + lang + "\n*** ERROR :file is empty\n";
        }
//        System.out.println("File Content:" + content);
        init(content);
    }

    /**
     * used to initialise a error msg
     */
    public SegDoc(String fname, String fromstring, String lang) {
        uri = fname;
        this.lang = lang;
        content = fromstring;
        init(content);
    }

    public void init(String document) {
        nblines = countLines(document);
        //("nblines:"+nblines);
        lines = getLines(document, nblines);

    }

    public static int countLines(String s) {
        int count = 0;
        try {
            Reader r = new StringReader(s);
            BufferedReader in = new BufferedReader(r);
            String w = in.readLine();

            while (w != null) {
                count++;
                w = in.readLine();
            }
            in.close();
            r.close();

        } catch (Exception e) {
            error("countLines", e);
        }
        return count;
    }

    public static String[] getLines(String s, int nblines) {
        String[] l = new String[nblines];
        int count = 0;
        try {
            Reader r = new StringReader(s);
            BufferedReader in = new BufferedReader(r);
            String w = in.readLine();

            while (w != null) {
                l[count] = w;
                count++;
                w = in.readLine();
            }
            in.close();
            r.close();

        } catch (Exception e) {
            error("getLines", e);
        }
        return l;
    }
}
