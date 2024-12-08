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
package org.olanto.zahir.create.bitext;

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
public class CreateASetOfBiTexts {

    static Vector<String> fileList;

    /**
     *
     */
    public int begin;

    /**
     *
     */
    public int end;

    /**
     *
     */
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

    /**
     *
     * @param _SO
     * @param _TA
     * @param _auto
     * @param _verbose
     * @param _fromfile
     * @param _tofile
     * @param _encoding
     * @param _limit
     * @param _s2t
     * @param _TMX
     * @param _writefile
     * @param _EXT
     */
    public CreateASetOfBiTexts(String _SO, String _TA, boolean _auto, boolean _verbose,  String _fromfile, String _tofile, String _encoding,
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

    /**
     *
     * @param fileList
     * @param _begin
     * @param _end
     */
    public CreateASetOfBiTexts(Vector<String> fileList, int _begin, int _end) {
        //fileList = fileList;
        begin = _begin;
        end = _end;
        size = end - begin + 1;
    }

    /**
     *
     */
    public void createBiTextMethod() {
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
            System.out.println("create bitext :" + i + " file:" + fromfile + "/" + name);
            CreateBiSentence bc = null;
//            try {
                bc = new CreateBiSentence(
                        auto, 5, 10,
                        verbose,
                        fromfile + "/" + name,
                        tofile + "/" + name,
                        "UTF-8",
                        4000,
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
    
    /**
     *
     * @param counttested
     * @param countTMX
     */
    public synchronized void updateCount(long counttested, int countTMX) {
        totcounttested += counttested;
        totcountTMX += countTMX;
    }

    /**
     *
     * @param s
     */
    public synchronized void log(String s) {
        try {
            out.write(s + "\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    /**
     *
     * @param subbegin
     * @param subend
     * @return
     */
    public CreateASetOfBiTexts subProblem(int subbegin, int subend) {
        return new CreateASetOfBiTexts(fileList, subbegin, subend);
    }

    void init(String f) {
        indexdir(f);
    }

    /**
     *
     */
    public void close() {
        try {
            msg("totAlign:" + totcountTMX);
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    public String getInfo() {
        return "size:" + size;
    }

    /**
     *
     * @param path
     */
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

    /**
     *
     * @param name
     */
    public void process(String name) {
        fileList.add(name);
    }
}
