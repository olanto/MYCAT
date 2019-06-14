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

package org.olanto.idxvli;

import static org.olanto.idxvli.IdxEnum.*;
import static org.olanto.idxvli.IdxConstant.*;

/**
 * Une classe pour collecter des informations statistiques sur l'indexeur.
 * 
 */
public class IdxStatistic {

    IdxStructure glue;

    IdxStatistic(IdxStructure id) {
        glue = id;
    }

    /** Affiche dans la console des statistiques sur l'indexeur.
     * <br><br><b>Wordmax</b>: nbr maximum de mots indexables.
     * <br><b>Wordnow</b>: nbr de mots indexÃ©s.
     * <br><b>used</b>: % d'utilisation.
     * <br><br><b>Documax</b>: nbr maximum de documents indexables.
     * <br><b>Docunow</b>: nbr de documents indexÃ©s.
     * <br><b>used</b>: % d'utilisation.
     * <br><br><b>totidx</b>: nbr d'entrÃ©es total dans l'index.
     * <br><b>totidxld</b>: nbr d'entrÃ©es chargÃ©es dans l'indexeur.
     * <br><b>load</b>: % des entrÃ©es en mÃ©moire.
     * <br><br><b>totidx</b>: nbr d'entrÃ©es total dans l'index.
     * <br><b>docunow</b>: nbr d'entrÃ©es dans le fichier.
     * <br><b>used</b>: % d'utilisation du fichier.
     * <br><br><b>dist10</b>: nbr de mots ayant de 1 Ã  9 occurences.
     * <br><b>dist100</b>: nbr de mots ayant de 10 Ã  99 occurences.
     * <br><b>dist1000</b>: nbr de mots ayant de 100 Ã  999 occurences.
     * <br>etc
     *
     */
    public String getGlobal() {
        return glue.lastRecordedWord+"\n"+glue.lastRecordedDoc+"\n"
                +"KDoc: " + DOC_MAX / 1024 + "/" + (glue.lastRecordedDoc / 1024) + " " + (int) (((double) glue.lastRecordedDoc * (double) 100) / (double) Math.max(DOC_MAX, 1)) + "%"
                + " - KWord: " + WORD_MAX / 1024 + "/" + (glue.lastRecordedWord / 1024) + " " + (int) (((double) glue.lastRecordedWord * (double) 100) / (double) Math.max(WORD_MAX, 1)) + "%";
    }

    public void global() {
        //Timer timing=new Timer("statistics");
        System.out.println("STATISTICS global:");
        System.out.println("wordmax: " + WORD_MAX + ", wordnow: " + glue.lastRecordedWord + ", used: " + (int) (((double) glue.lastRecordedWord * (double) 100) / (double) WORD_MAX) + "%");
        System.out.println("documax: " + DOC_MAX + ", docunow: " + (glue.lastRecordedDoc) + ", used: " + (int) (((double) glue.lastRecordedDoc * (double) 100) / (double) DOC_MAX) + "%");
        if (glue.lastRecordedDoc != 0) {
            System.out.println("docIdx: " + (glue.lastRecordedDoc) + " docValid: " + (glue.docstable.countValid() + ", valid: " + (int) (((double) glue.docstable.countValid() * (double) 100) / (double) glue.lastRecordedDoc)) + "%");
        }
        long totidx = 0;
        long totidxld = 0;
        long totidxpos = 0;
        if (MODE_IDX == IdxMode.QUERY && FULL_LOAD) {
            for (int i = 0; i < glue.lastRecordedWord; i++) {
                totidx += glue.getOccOfW(i);
            }
        }
        System.out.println("totidx: " + totidx);
        if (IDX_SAVE_POSITION) {
            System.out.println("totidxpos: " + glue.cntpos);
        }
        long minspace = 0;
        if (IDX_SAVE_POSITION) {
            minspace = (8 * totidx + 4 * glue.cntpos) / 1024 / 1024;
        } else {
            minspace = (4 * totidx) / 1024 / 1024;
        }

        long usespace = 0;

//            for (int i=0;i<OBJ_NB;i++){
//                usespace+=(glue.IO.objsto[0].getSpace()/1024/1024)+1;
//            }
//            System.out.println("min space(Mb): "+minspace+", used space(Mb): "+usespace+", used: "+((minspace*100)/usespace)+"%");
//            
//            int dist10=0;
//            int dist100=0;
//            int dist1000=0;
//            int dist10000=0;
//            int dist100000=0;
//            int dist1000000=0;
//            int dist10000000=0;
//            int dist100000000=0;
//            for (int i=0 ;i<glue.lastword; i++) {
//                if (glue.getOccOfW(i)<10) dist10++;
//                else if (glue.getOccOfW(i)<100) dist100++;
//                else if (glue.getOccOfW(i)<1000) dist1000++;
//                else if (glue.getOccOfW(i)<10000) dist10000++;
//                else if (glue.getOccOfW(i)<100000) dist100000++;
//                else if (glue.getOccOfW(i)<1000000) dist1000000++;
//                else if (glue.getOccOfW(i)<10000000) dist10000000++;
//                else if (glue.getOccOfW(i)<100000000) dist100000000++;
//            }
//            System.out.println("DISTRIBUTION:");
//            System.out.println("dist10      : "+dist10);
//            System.out.println("dist100     : "+dist100);
//            System.out.println("dist1000    : "+dist1000);
//            System.out.println("dist10000   : "+dist10000);
//            System.out.println("dist100000  : "+dist100000);
//            System.out.println("dist1000000 : "+dist1000000);
//            System.out.println("dist100000000: "+dist100000000);

        glue.wordstable.printStatistic();
        if (FULL_LOAD) glue.docstable.printStatistic();

        //timing.stop();
    }

    /**
     * Affiche dans la console des statistiques sur les mots les plus indexÃ©s dans le corpus.
     * <br>affiche le mot et la longeur de son index.
     * @param limitlength les mots doivent avoir une longueur d'index dÃ©passant cette limite
     */
    public void topIndexByLength(int limitlength) {
        System.out.println("TOP (length) index (word,nbdoc,length):");
        for (int i = 0; i < glue.lastRecordedWord; i++) {
            if (IDX_SAVE_POSITION) {
                if (glue.getOccOfW(i) > limitlength) {
                    System.out.println(glue.wordstable.get(i) + ": " + glue.getOccOfW(i) + ": " + glue.getOccCorpusOfW(i));
                }
            } else {
                if (glue.getOccOfW(i) > limitlength) {
                    System.out.println(glue.wordstable.get(i) + ": " + glue.getOccOfW(i));
                }
            }
        }
    }

    /**
     * Affiche dans la console des statistiques sur les mots les plus indexÃ©s dans les documents.
     * <br>Affiche le mot et le nbr de documents dans lequel il apparaÃ®t.
     * @param limitdoc les mots doivent un certain pourcentage de documents (par ex 80%)
     */
    public void topIndexByDoc(int limitdoc) {
        System.out.println("TOP (occ in >" + limitdoc + "% Doc) index (word,nbdoc,length):");
        int occofW = (int) 0;
        for (int i = 0; i < glue.lastRecordedWord; i++) {

            occofW = glue.getOccOfW(i);
            // System.out.println(i+","+occofW+","+glue.lastdoc);
            if (IDX_SAVE_POSITION) {
                if ((occofW * 100) / glue.lastRecordedDoc > limitdoc) {
                    System.out.println(glue.wordstable.get(i) + ": " + glue.getOccOfW(i) + ": " + glue.getOccCorpusOfW(i));
                }
            } else {
                if ((occofW * 100) / glue.lastRecordedDoc > limitdoc) {
                    System.out.println(glue.wordstable.get(i) + ": " + glue.getOccOfW(i));
                }
            }
        }

    }
}
