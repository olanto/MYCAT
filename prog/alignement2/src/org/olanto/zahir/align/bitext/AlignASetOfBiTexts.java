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
package org.olanto.zahir.align.bitext;

import org.olanto.idxvli.IdxStructure;
import org.olanto.zahir.align.LexicalTranslation;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.Vector;
import static org.olanto.util.Messages.*;

/**
 *
 * @author jg
 */
public class AlignASetOfBiTexts {

    static Vector<String> fileList;
    public int begin;
    public int end;
    public int size;
    static boolean auto, verbose, writefile;
  //  static IdxStructure id;
    static String fromfile, tofile, encoding, EXT;
    static float limit;
    static LexicalTranslation s2t;
    static String TMX;
    static long totcounttested;
    static long count, start, totcountTMX;
    static String SO, TA;
    private static OutputStreamWriter out;

    public AlignASetOfBiTexts(String _SO, String _TA, boolean _auto, boolean _verbose,  String _fromfile, String _tofile, String _encoding,
            float _limit, LexicalTranslation _s2t, String _TMX, boolean _writefile, String _EXT) {
        SO = _SO;
        TA = _TA;
        auto = _auto;
        verbose = _verbose;
        writefile = _writefile;
         fromfile = _fromfile;
        tofile = _tofile;
        encoding = _encoding;
        limit = _limit;
        s2t = _s2t;
        TMX = _TMX;
        EXT = _EXT;

        fileList = new Vector<String>();
        init(fromfile);
        begin = 0;
        end = fileList.size() - 1;
        size = fileList.size();
        try {
            out = new OutputStreamWriter(new FileOutputStream(TMX + "/_log.txt"), "UTF-8");
        } catch (Exception e) {
            System.err.println("IO error:");
            e.printStackTrace();
        }
    }

    public AlignASetOfBiTexts(Vector<String> fileList, int _begin, int _end) {
        //fileList = fileList;
        begin = _begin;
        end = _end;
        size = end - begin + 1;
    }

    public void alignSeqMethod() {
        //System.out.println("align SEQ:" + begin + ".." + end);
        for (int i = begin; i <= end; i++) {
            if (count % 100 == 0) {
                if (count == 0) {
                    start = System.currentTimeMillis();
                } else {
                    long stop = System.currentTimeMillis();
                    System.out.println("count:" + count + " time ms:" + (stop - start)
                            + " tested K/s:" + (totcounttested / (stop - start)));
                }
            }
            count++;

            String name = fileList.get(i);
            // System.out.println("align:" + i + " file:" + fromfile + "/" + name);
            BiSentence bc = null;
//            try {
                bc = new BiSentence(
                        auto, 10, 10,
                        verbose,
                        fromfile + "/" + name,
                        tofile + "/" + name,
                        "UTF-8",
                        1000,
                        3,
                        s2t);
                if (!bc.error) {
                    bc.buildCertainMap(TMX + "/" + name, SO, TA);
                    log(bc.getInformation());
                    updateCount(bc.counttested, bc.countTMX);
                    // System.out.println("align:"+i+" count:"+bc.countalign+" loop1:"+bc.countloop1);
                }

//            } catch (Exception ex) {
//                msg("error for: " + fromfile+ "/" + name + " <-> " + tofile+ "/" + name);
//        }
    }
    }

    public synchronized void updateCount(long counttested, int countTMX) {
        totcounttested += counttested;
        totcountTMX += countTMX;
    }

    public synchronized void log(String s) {
        try {
            out.write(s + "\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public AlignASetOfBiTexts subProblem(int subbegin, int subend) {
        return new AlignASetOfBiTexts(fileList, subbegin, subend);
    }

    void init(String f) {
        indexdir(f);
    }

    public void close() {
        try {
            msg("totAlign:" + totcountTMX);
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getInfo() {
        return "size:" + size;
    }

    public void indexdir(String path) {
        File f = new File(path);
        if (f.isFile()) {
            //System.out.println("path:"+f);
            if (path.endsWith(EXT)) {
                process(f.getName());
            }
        } else {
            String[] lf = f.list();
            int ilf = Array.getLength(lf);
            for (int i = 0; i < ilf; i++) {
                indexdir(path + "/" + lf[i]);
            }
        }
    }

    public void process(String name) {
        fileList.add(name);
    }
}
