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
package org.olanto.mycat.tmx.support;

import org.olanto.conman.server.GetContentService;
import java.rmi.*;
import org.olanto.idxvli.server.*;
import org.olanto.idxvli.util.SetOperation;
import static org.olanto.util.Messages.*;
import org.olanto.util.Timer;

/**
 * test une recherche
 *
 */
public class TestClientQuerySimple {

    static IndexService_MyCat is;
    final static float NTOT = 100000000;

    public static void main(String[] args) {

        is = GetContentService.getServiceMYCAT("rmi://localhost/VLI");
        try {
            showVector(is.getDictionnary().result);
            showVector(is.getCorpusLanguages());
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }


//test("armement OR nucléaire");
//test("weapon IN[\"SOURCE.EN\" AND \"TARGET.RU\"]");
//test("(weapon AND nuclear)IN[\"SOURCE.EN\" AND \"TARGET.RU\"]");
//test("NEAR(\"weapon\",\"nuclear\")IN[\"SOURCE.EN\" AND \"TARGET.RU\"]");
//test("QUOTATION(\"efforts to mainstream gender perspectives\") IN[\"SOURCE.EN\" AND \"TARGET.FR\"]");
//test("QUOTATION(\"financial the collappse\") ");

//        test("QUOTATION(\"final report\") IN[\"SOURCE.EN\"]");
//       test("QUOTATION(\"rapport final\") IN[\"SOURCE.FR\"]");

//        correlation("rapport final", "final report", "FR", "EN");
//        correlation("rapport final", "finax report", "FR", "EN");
//       correlation("rapport", "report", "FR", "EN");
//      correlation("table", "table", "FR", "EN");
//      correlation("voiture", "car", "FR", "EN");
//     correlation("menu", "menu", "FR", "EN");
//    correlation("Annulation du jugement", "mistrial", "FR", "EN");
//    correlation("amende", "Financial penalty", "FR", "EN");
//    correlation("amende", "penalty", "FR", "EN");
//   correlation("appel", "appeal", "FR", "EN");
//   correlation("assistance judiciaire", "legal assistance", "FR", "EN");
//   correlation("assistance juridique", "legal assistance", "FR", "EN");
//   correlation("aide juridictionnelle", "legal assistance", "FR", "EN");
        
      correlation("assistance judiciaire", "legal aid", "FR", "EN");  
      correlation("assistance judiciaire", "legal assistance", "FR", "EN"); 
      correlation("assistance judiciaire", "judicial assistance", "FR", "EN"); 
      
      correlation("choc psychologique", "mental shock", "FR", "EN"); 
     correlation("choc psychologique", "psychological trauma", "FR", "EN"); 
     correlation("choc psychologique", "psychological shock", "FR", "EN"); 
   
      correlation("Détention illégale", "illegal detention", "FR", "EN"); 
      correlation("Détention illégale", "unlawful detention", "FR", "EN"); 
     
      
    }

    static void test(String query) {
        try {
            Timer t1 = new Timer("------------- " + query);
            QLResultNice res = is.evalQLNice(query, 0, 200);
            msg("time:" + res.duration);
            msg("nbres:" + res.result.length);
            for (int i = 0; i < res.result.length; i++) {
                msg(i + "  docid: " + res.result[i]);
                msg("  docname: " + res.docname[i]);
                msg("");
            }
            t1.stop();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    static double correlation(String termso, String termta, String langso, String langta) {
        try {
            String queryso = "QUOTATION(\"" + termso + "\") IN[\"SOURCE." + langso + "\"]";
            String queryta = "QUOTATION(\"" + termta + "\") IN[\"SOURCE." + langta + "\"]";
            Timer t1 = new Timer("------------- " + queryso+" -> "+queryta);
            QLResultNice resso = is.evalQLNice(queryso, 0, 0);
            QLResultNice resta = is.evalQLNice(queryta, 0, 0);
            //msg("time:" + resso.duration);
            float n1 = resso.result.length;
            msg("n1:" + resso.result.length);
            //msg("time:" + resta.duration);
            float n2 = resta.result.length;
            msg("n2:" + resta.result.length);
            for (int i = 0; i < resta.result.length; i++) { // adjust value to source
                resta.result[i]--;
            }
            int[] interserct = SetOperation.and(resso.result, resta.result);
            msg("n12:" + interserct.length);
            double n12 = interserct.length;
            double num = NTOT * n12 - n1 * n2;
            double den = Math.sqrt(NTOT * n1 - n1 * n1) * Math.sqrt(NTOT * n2 - n2 * n2);
            msg("den:" + den);
            double corelation = 0;
            if (den != 0) {
                corelation = num / den;
            }

            msg("corelation:" + corelation);
            t1.stop();
            return corelation;
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return 0;
        }


    }
}
