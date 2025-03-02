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

package org.olanto.server.test;

import org.olanto.idxvli.*;
import org.olanto.util.Timer;
import java.io.*;
import org.olanto.idxvli.ql.*;
import static org.olanto.util.Messages.*;
import org.olanto.util.TimerNano;

/**
 * *
 * 
 *
 *
 * Test de l'indexeur, mode query
 */
public class TESTQL_CLASSIC {

    private static IdxStructure id;
    private static Timer t1 = new Timer("global time");

    /**
     * application de test
     * @param args sans
     */
    public static void main(String[] args) {

        id = new IdxStructure("INCREMENTAL", new ConfigurationNative());
        id.Statistic.global();
        //id.checkIntegrityOfW("nana",true);
        //testref();


//        test("select");
//        test("hashtable");
//        test("hashtable AND interface");
//        test("NEAR(garbage,collection)");
//        test("NEXT(create,view)");


        // test avec BILANG ... book
//        test("nana");
//        test("kopeks");
//        test("russie");
//        test("oui");
//        test("lune");
//        test("kopeks AND russie");
//        test("nana AND anna");
        test("charbon AND mine");
        test("charbon mine");
        test("(charbon) (mine)");
        test("charbon ~(noir mine)");
        test("nana OR lune");
        test("nana | lune");
        test("nana MINUS anna");
        test("nana - anna");
        test("NEAR(belle,femme)");
        test("~(belle,femme)");
        test("~(belle femme)");
//        test("NEXT(pommes,terre)");
        test("QUOTATION(\"un jeune homme qui venait achever son\")");
        test("\"un jeune homme qui venait achever son\"");
//        test("QUOTATION(\"un jeune homme qui venait\")");
//        test("QUOTATION(\"un jeune homme qui\")");
//        test("pari");
//        test("fasdjfasfdj");
        test("chabron ~(noir AND mine) (nana OR lune) \"un jeune homme\" 2006 MINUS anna");


        t1.stop();
        id.close();
    }

    /**
     *
     * @param s
     */
    public static void test(String s) {
        TimerNano t1 = new TimerNano("parse:" + s, false);

        QLCompiler ql = new QLCompiler(new StringReader(s), id);
        int[] res = ql.execute();
        t1.stop(false);
        if (res == null) {
            msg("result is null");
        } else {
            msg("result is ndoc:" + res.length);
            for (int i = 0; i < res.length; i++) {
                msg("  doc " + res[i] + " : " + id.getFileNameForDocument(res[i]));
            }
        }
        msg("\n-----------------------------------------------------------------------------\n");
        //showVector(res);


    }

    private static void testref() {
        String s = readFromFile("C:/JG/gigaversion/data/REFTEST.TXT");

        {
            TimerNano t1 = new TimerNano("ref 1-----------:", false);
            id.Query.parseQuery("MULTI", s, "6");
            t1.stop(false);
        }

//        {TimerNano t1=new TimerNano("ref 2-----------:",false);
//        id.Query.parseQuery("MULTI",s,"6");
//        t1.stop(false);
//        }

        //System.out.println(id.Query.parseQuery("MULTI",s,"6"));
    }

    private static String readFromFile(String fname) {
        StringBuffer b = new StringBuffer("");
        try {
            BufferedReader in = new BufferedReader(new FileReader(fname));
            String w = in.readLine();
            while (w != null) {
                //System.out.println(w);
                b.append(w);
                w = in.readLine();
            }
        } catch (Exception e) {
            System.err.println("IO error in SetOfWords");
        }
        return b.toString();
    }
}
