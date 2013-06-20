/**********
    Copyright © 2010-2012 Olanto Foundation Geneva

   This file is part of myCAT.

   myCAT is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of
    the License, or (at your option) any later version.

    myCAT is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with myCAT.  If not, see <http://www.gnu.org/licenses/>.

**********/
package org.olanto.zahir.align.comparable;

import org.olanto.idxvli.IdxStructure;
import java.io.File;
import java.lang.reflect.Array;
import java.util.Vector;
import org.olanto.zahir.align.LexicalTranslation;

/**
 *
 * @author jg
 */
public class AlignASetOfComparables {

    static Vector<String> fileList;
    public int begin;
    public int end;
    public int size;
    static boolean  verbose, writefile;
    static String fromfile, tofile, encoding, EXT;
    static float limit;
    static LexicalTranslation s2t;
    static CollectAndSave saveFile;
    static long totcounttested;
    static long count, start;

    public AlignASetOfComparables(
            boolean _verbose,
            String _fromfile,
            String _tofile,
            String _encoding,
            float _limit,
            LexicalTranslation _s2t,
            CollectAndSave _saveFile,
            boolean _writefile,
            String _EXT) {
        verbose = _verbose;
        writefile = _writefile;
        fromfile = _fromfile;
        tofile = _tofile;
        encoding = _encoding;
        limit = _limit;
        s2t = _s2t;
        saveFile = _saveFile;
        EXT = _EXT;

        fileList = new Vector<String>();
        init(fromfile);
        begin = 0;
        end = fileList.size() - 1;
        size = fileList.size();
    }

    public AlignASetOfComparables(Vector<String> fileList, int _begin, int _end) {
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
            //System.out.println("align:"+i);
            String name = fileList.get(i);
            //if (i==3){
            BiComparable bc = new BiComparable(
                    i,
                    verbose,
                    fromfile + "/" + name,
                    tofile + "/" + name,
                    "UTF-8",
                    0.2f,
                    s2t,
                    saveFile,
                    true);
            updateCount(bc.counttested);
            // System.out.println("align:"+i+" count:"+bc.countalign+" loop1:"+bc.countloop1);

        }
    }

    public synchronized void updateCount(long count) {
        totcounttested += count;
    }

    public AlignASetOfComparables subProblem(int subbegin, int subend) {
        return new AlignASetOfComparables(fileList, subbegin, subend);
    }

    void init(String f) {
        indexdir(f);
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
