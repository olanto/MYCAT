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
package org.olanto.mycat.tmx.experiments;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.olanto.conman.server.GetContentService;
import java.rmi.*;
import java.util.HashMap;
import org.olanto.idxvli.server.*;
import org.olanto.idxvli.util.SetOperation;
import org.olanto.mycat.tmx.dgt2014.extract.LangMap;
import static org.olanto.util.Messages.*;
import org.olanto.util.Timer;

/**
 * test une recherche
 *
 */
public class Test2014DEEN {

    static IndexService_MyCat is;
    final static float NTOT = 100000000;

    public static void main(String[] args) {

        is = GetContentService.getServiceMYCAT("rmi://localhost/VLI");
           LangMap.init();
     try {
            showVector(is.getDictionnary().result);
            showVector(is.getCorpusLanguages());
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }

        getGlossary("C:\\CORPUS\\TERM\\english-german-clean.txt.clean");

    }

    static void getGlossary(String fileName) {
        InputStreamReader isr = null;
        int count = 0;
        try {

            isr = new InputStreamReader(new FileInputStream(fileName)); //, "UTF-8");
            BufferedReader in = new BufferedReader(isr);
            String w;
            w = in.readLine();
            //System.out.println(w);
            while (w != null) {
                count++;
                String[] part = w.split("\t");
                if (part.length == 2) {
                    correlation(part[1].replace("\"", ""), part[0].replace("\"", ""), "DE", "EN");
                }
                w = in.readLine(); //System.out.println(w);
            }
            System.out.println("Tested lines:" + count + " from " + fileName);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    static double correlation(String termso, String termta, String langso, String langta) {
        try {
              String queryso = "QUOTATION(\"" + termso + "\") IN[\"SOURCE." + langso + "\" ANDL \"TARGET." + langta+"\"]";
            String queryta = "QUOTATION(\"" + termta + "\") IN[\"SOURCE." + langta + "\" ANDL \"TARGET." + langso+"\"]";
       //    Timer t1 = new Timer("------------- " + queryso + " -> " + queryta);
            QLResultNice resso = is.evalQLNice(queryso, 0, 0);
            QLResultNice resta = is.evalQLNice(queryta, 0, 0);
            //msg("time:" + resso.duration);
            float n1 = resso.result.length;
            System.out.print(termso+"\t"+termta+"\t");
            System.out.print(resso.result.length+"\t");
            //msg("time:" + resta.duration);
            float n2 = resta.result.length;
            System.out.print(resta.result.length+"\t");
            for (int i = 0; i < resta.result.length; i++) { // adjust value to source
                resta.result[i]+=LangMap.deltaSOTA(langso, langta);
            }
            int[] interserct = SetOperation.and(resso.result, resta.result);
            System.out.print(interserct.length+"\t");
            double n12 = interserct.length;
            double num = NTOT * n12 - n1 * n2;
            double den = Math.sqrt(NTOT * n1 - n1 * n1) * Math.sqrt(NTOT * n2 - n2 * n2);
            //msg("den:" + den);
            double corelation = 0;
            if (den != 0) {
                corelation = num / den;
            }

            System.out.print(corelation+"\n");
         //   t1.stop();
            return corelation;
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return 0;
        }


    }
}
