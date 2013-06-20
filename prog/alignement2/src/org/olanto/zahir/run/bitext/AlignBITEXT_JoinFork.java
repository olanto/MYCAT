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
package org.olanto.zahir.run.bitext;





/**
 *
 * une classe pour aligner en utilisatant plusieurs core.
 * doit être compilée avec java 1.7
 * 
 *  uncomment to active!
 */


/*
 * 
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import org.olanto.idxvli.IdxStructure;
import org.olanto.util.Timer;
import org.olanto.zahir.align.LexicalTranslation;
import org.olanto.zahir.align.bitext.AlignASetOfBiTexts;
import org.olanto.zahir.align.comparable.AlignASetOfComparables;
import org.olanto.zahir.align.comparable.CollectAndSave;

public class AlignBITEXT_JoinFork extends RecursiveAction {

    static int SMALL = 1;
    private static IdxStructure id;
    private static Timer t1 = new Timer("global time");
    private static CollectAndSave saveFile;
    static String FN1, EXT, FN2, TMX, LOG, DIC;
    static int count;
    private static LexicalTranslation s2t;
    static AlignASetOfBiTexts initalignSet;
    AlignASetOfBiTexts alignSet;
    private static double tSEQ;

    public static void main(String[] args) {

        String run = "";

//        FN1 = "E:/CERN/cernsegbylang/EN"+run;
//        FN2 = "E:/CERN/cernsegbylang/FR"+run;
//        TMX = "E:/CERN/cernsegbylang/TMX-ENFR"+run;

//        FN1 = "E:/UPU/upusegbylang/EN"+run;
//        FN2 = "E:/UPU/upusegbylang/FR"+run;
//        TMX = "E:/UPU/upusegbylang/TMX-ENFR"+run;

        FN1 = "E:/OMC/omcsegbylang/EN" + run;
        FN2 = "E:/OMC/omcsegbylang/FR" + run;
        TMX = "E:/OMC/omcsegbylang/TMX-ENFR" + run;


        DIC = "C:/MYALIGN/map/ENFR/lex.e2f";
        // attention à la liste des stops word !!
        EXT = ".txt";


        id = new IdxStructure(); // indexeur vide
        id.initComponent(new ConfigurationIDX()); // pour initialiser le parseur ...
        s2t = new LexicalTranslation(DIC, "UTF-8", 0.1f);
        saveFile = new CollectAndSave(TMX);


        initalignSet = new AlignASetOfBiTexts(
                true,
                false,
                id,
                FN1,
                FN2,
                "UTF-8",
                0.2f,
                s2t,
                TMX,
                true,
                EXT);

        System.out.println(initalignSet.getInfo());

        //double tSEQ = (double) SEQMethod(alignSet);
        double tSEQ = 211.0;

//        compare(alignSet, 2, 4);
//        compare(alignSet, 2, 8);
//        compare(alignSet, 2, 16);
//        compare(alignSet, 2, 32);
//        compare(alignSet, 2, 1024);
//        compare(alignSet, 4, 4);
        //       compare(initalignSet, 8, 8);
        //       compare(initalignSet, 1, 1);
        compare(initalignSet, 8, 2048);
//        compare(alignSet, 8, 32);
        System.out.println("end ...");
        saveFile.close();
        initalignSet.close();
    }

    public AlignBITEXT_JoinFork(AlignASetOfBiTexts alignSet) {
        this.alignSet = alignSet;
    }

    public AlignBITEXT_JoinFork(AlignASetOfBiTexts m, int SMALL) {
        this.alignSet = m;
        this.SMALL = SMALL;
    }

    protected void compute() {
        //System.out.println(" compute ["+alignSet.begin+".."+alignSet.end+"] size="+alignSet.size+" SMALL="+SMALL);
        if (alignSet.size < SMALL) {
            // System.out.println(" TO SMALL=");
            alignSet.alignSeqMethod();
        } else {
            int middle = alignSet.begin + alignSet.size / 2;
            // System.out.println("CUT ["+alignSet.begin+".."+middle+"] & ["+(middle + 1)+".."+alignSet.end+"]");
            AlignBITEXT_JoinFork left = new AlignBITEXT_JoinFork(alignSet.subProblem(alignSet.begin, middle));
            AlignBITEXT_JoinFork right = new AlignBITEXT_JoinFork(alignSet.subProblem(middle + 1, alignSet.end));
            invokeAll(left, right);
        }
    }

    public static void compare(AlignASetOfBiTexts alignSet, int nbThread, int smallRatio) {
        double tFJ = (double) FJMethod(alignSet, nbThread, smallRatio);

        System.out.println("ratio:" + tSEQ / tFJ + " nbThread:" + nbThread + " smallRatio:" + smallRatio);

    }

    public static long FJMethod(AlignASetOfBiTexts alignSet, int nbThread, int smallRatio) {
        AlignBITEXT_JoinFork init = new AlignBITEXT_JoinFork(alignSet, Math.max(4, alignSet.size / smallRatio));
        //saveFile.reset();
        long start = System.nanoTime();
        ForkJoinPool fjPool = new ForkJoinPool(nbThread);
        fjPool.invoke(init);
        // saveFile.close();
        long duration = (System.nanoTime() - start) / 1000000000;
        //   System.out.println("FJ max:"+init.result+ " duration:"+duration/1000000+" [ms]");
        return duration;
    }

    public static long SEQMethod(AlignASetOfComparables alignSet) {
        long start = System.nanoTime();
        //saveFile.reset();
        alignSet.alignSeqMethod();
        //saveFile.close();
        long duration = System.nanoTime() - start;
        System.out.println("Seq duration:" + duration / 1000000000 + " [s]");
        return duration;
    }
}
*/