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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Classe pour charger un lexique de traduction.
 * 
 */
public class LexicalTranslation {

    HashMap<String, ArrayList<Double>> lexreverse = new HashMap<String, ArrayList<Double>>(300000);
    HashMap<String, Float> lexmap = new HashMap<String, Float>(300000);
    HashMap<String, Integer> lexentryso = new HashMap<String, Integer>(150000);
    HashMap<Integer, String> lexidso = new HashMap<Integer, String>(150000);
    int count;
    int countso;

    public LexicalTranslation(String source, String sourceEncoding, float limit) {
        BufferedReader input = null;
        try {
            System.out.println("LexicalTranslation from :"+source);
            input = new BufferedReader(new InputStreamReader(new FileInputStream(source), sourceEncoding));

            Pattern p = Pattern.compile("[\\s]");  // les blancs
            String w;
            w = input.readLine();
            while (w != null) {
                String[] efv = p.split(w); //String[] cible source value;
                // msg(efv[0]+"-"+efv[1]+"-"+efv[2]);
                double x = Double.parseDouble(efv[2]);
                if (x == 1.0) {
                    x = 0.999;
                }
                if (x > limit) {
                    lexmap.put(efv[0] + " " + efv[1], (float) x);
                    Integer soid = lexentryso.get(efv[0]);
                    if (soid == null) { // ajoute une entrÃ©e
                        lexentryso.put(efv[0], countso);
                        lexidso.put(countso, efv[0]);
                        soid = countso;
                        countso++;
                    }
                    ArrayList talist = lexreverse.get(efv[1]);
                    if (talist == null) { // ajoute une entrÃ©e
                        ArrayList nl = new ArrayList<Double>(2);
                        nl.add((double) soid + x); // stock id et le score (entre 0.999 et limit) limit!=0;
                        lexreverse.put(efv[1], nl);
                    } else {
                        talist.add((double) soid + x);
                    }
                }
                w = input.readLine();
                count++;
            }
            input.close();
            System.out.println(source + " #" + count + " source entries" + " #" + lexmap.size() + " keep entries" + " #" + lexentryso.size() + " keep terms");

//            ArrayList talist = lexreverse.get("suisse");
//            System.out.println("suisse");
//            for (int i = 0; i < talist.size(); i++) {
//                double idxso = (Double) talist.get(i);
//                int idso = (int) idxso;
//                System.out.println(idxso + " -> " + lexidso.get(idso));
//
//            }


        } catch (Exception ex) {
//            Logger.getLogger(LexicalTranslation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                input.close();
            } catch (IOException ex) {
                Logger.getLogger(LexicalTranslation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String display(int[] v) {
        String s = "";
        for (int i = 0; i < v.length; i++) {
            s += " " + lexidso.get(v[i]);
        }
        return s;
    }
}
