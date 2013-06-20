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

package org.olanto.dtk.test;

import java.io.*;
import org.olanto.util.Timer;
import org.olanto.dtk.*;

/**
 * permet de générer des familles de documents synthétiques à partir d'un automate de Markov.
 * <p>
 * La famille de documents synthétique est au format MFL, on choisit la langue, l'ordre de l'automate,
 * le nombre de documents, la taille des documents
 * <p>
 *
 */
public class GenerateMFL {

    /**
     * permet d'effectuer le test de cette classe
     * @param args pas utilisés
     */
    public static void main(String args[]) {
        int tokensize = 2;
        MAutomata french = new MAutomata("C:/JG/gigaversion/TrainingDTK/allFrench.txt", 8200000, tokensize);
        Timer t1 = new Timer("generate:");
        //  generate(french,"C:/JG/gigaversion/isi/jg/dtk/test_","a",872000/tokensize,20);
        generate(french, "C:/JG/gigaversion/TrainingDTK/test_", "f", 10000 / tokensize, 10000);
        t1.stop();
    }

    /**
     * génération automatique d'un document mfl
     * @param a l'automate de Markov entrainé avec une langue donnée
     * @param filename fichier mfl à générer
     * @param prefix à ajouter au nom du fichier mfl, pour la génération d'un ensemble de mfl
     * @param size nbr de token générés, la taille = size * tokensize, oû tokensize est l'ordre de l'automate
     * @param nbdoc nbr de documents dans le fichier mfl
     */
    public static void generate(MAutomata a, String filename, String prefix, int size, int nbdoc) {

        try {
            FileWriter out = new FileWriter(filename + prefix + ".mfl");
            for (int i = 0; i < nbdoc; i++) {
                if (i % 1000 == 0) {
                    System.out.println(i);
                }
                out.write("#####" + prefix + i + "#####\n");
                out.write(a.generateText(size) + "\n");
            }
            out.write("#####ENDOFFILE#####\n");
            out.flush();
            out.close();
        } catch (Exception e) {
            System.err.println("IO error ExportEntry");
            e.printStackTrace();
        }


    }
}
