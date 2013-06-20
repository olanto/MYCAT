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

package org.olanto.zahir.align;

import org.olanto.idxvli.IdxStructure;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import static org.olanto.idxvli.util.BytesAndFiles.*;
import static org.olanto.util.Messages.*;


/* 
 * Une classe pour stocker un document sous forme de phrase.
 *   * 
 */
public class DocumentSentence {

    public Sentence[] lines;
 //   IdxStructure glue;
    public int nblines;
    public int totNumbers;

    public DocumentSentence(String document) {
        init(document);
    }

    public DocumentSentence(String fname, String txt_encoding) {
        init(file2String(fname, txt_encoding));
    }

    public void init(String document) {
        nblines = Sentence.countLines(document);
        // msg("nblines:"+nblines);
        lines = Sentence.getLines(document, nblines);

        //lines[0].dump();lines[1].dump();lines[2].dump();
        //sumNumbers();
        //msg("totNumbers:"+totNumbers);
        // dumpNumbers();
    }

    /**
     * lit le contenu d'un fichier texte encodÃ©
     * @param fname nom du fichier
     * @param txt_encoding encodage
     * @return le contenu du fichier
     */
    public static final String file2String(String fname, String txt_encoding) {
        StringBuilder txt = new StringBuilder("");
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(fname), txt_encoding);
            BufferedReader in = new BufferedReader(isr);
            String w = in.readLine();
            while (w != null) {
                txt.append(w);
                txt.append("\n");
                w = in.readLine();
            }
            return txt.toString();
        } catch (Exception e) {
            //error("file2String", e);
            return null;
        }

    }

    public void convert2id(LexicalTranslation lex) {
        for (int i = 0; i < lines.length; i++) {
            lines[i].convert2id(lex);
        }
        //  lines[0].dump();
    }

    public void convert2idWithScore(LexicalTranslation lex) {
        for (int i = 0; i < lines.length; i++) {
            lines[i].convert2idWithScore(lex);
        }
        //   lines[0].dump();
    }

    public void dumpNumbers() {
        msg("totNumbers:" + totNumbers);
        for (int i = 0; i < lines.length; i++) {
            lines[i].dumpNumbers();
        }
    }

    public void sumNumbers() {
        totNumbers = 0;
        for (int i = 0; i < lines.length; i++) {
            totNumbers += lines[i].countNumbers;
        }
    }
}
