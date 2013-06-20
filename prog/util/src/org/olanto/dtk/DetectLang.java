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

package org.olanto.dtk;

/**
 * génération d'un détecteur de langues.
 *
 *on peut utiliser ces classes comme dans l'exemple suivant:
<pre>
package isi.jg.dtk;
public class TestDetectLang {
public static void main(String args[]) {
DetectLang d=new DetectLang(4);
d.trainLang("FRE","C:/JG/gigaversion/isi/jg/dtk/TrainingDTK/French.txt");
d.trainLang("ENG","C:/JG/gigaversion/isi/jg/dtk/TrainingDTK/English.txt");
d.trainLang("GER","C:/JG/gigaversion/isi/jg/dtk/TrainingDTK/German.txt");
d.trainLang("SPA","C:/JG/gigaversion/isi/jg/dtk/TrainingDTK/Spanish.txt");
String s1="Ceci est une phrase un peu plus longue.";
String s2="Provides the classes necessary to create an applet and the.";
String s3="According to a major industry study issued earlier this year.";
String s4="Il aime les pommes et les poires qui viennent du jardin.";
String s5="La france est un beau pays.";
String s6="England is a nice country.";
System.out.println(s1+" : "+d.langOfText(s1));
System.out.println(s2+" : "+d.langOfText(s2));
System.out.println(s3+" : "+d.langOfText(s3));
System.out.println(s4+" : "+d.langOfText(s4));
System.out.println(s5+" : "+d.langOfText(s5));
System.out.println(s6+" : "+d.langOfText(s6));
}
}
</pre>
 */
public class DetectLang {

    /** nbr de langues maximum de ce détecteur*/
    private int nbLang;
    /** nbr de langues actives de ce détecteur*/
    private int lastLang = 0;
    /** étiquettes des codes langues*/
    private String[] codeLang;
    /** vecteurs d'automates correspondant aux différentes langues*/
    private MAutomata[] MLang;

    /**
     * création d'un détecteur de langues
     * @param _nbLang nbre de langues maximum é détecter
     */
    public DetectLang(int _nbLang) {
        nbLang = _nbLang;
        codeLang = new String[nbLang];
        MLang = new MAutomata[nbLang];
    }

    /**
     * Apprentissage des caractéristiques de la langue
     * @param _codeLang code de langue (FRE,ENG,...)
     * @param _trainingFile fichier pour l'apprentissage
     */
    public void trainLang(String _codeLang, String _trainingFile) {
        codeLang[lastLang] = _codeLang;
        MLang[lastLang] = new MAutomata(_trainingFile);
        lastLang++;
    }

    /**
     * Evaluation de la langue d'un texte
     * @param txt texte é évaluer
     * @return code de la langue présumée de ce texte
     */
    public String langOfText(String txt) {
        // Timer t1=new Timer("detect");
        //System.out.println(lastLang);
        double[] prob = new double[lastLang];
        for (int i = 0; i < lastLang; i++) {
            prob[i] = -MLang[i].probOfSentence(txt);
            //System.out.println(i+","+prob[i]);
        }
        int top = topLang(prob);
        //t1.stop();
        if (top == -1) {
            return "ERROR";
        }
        return codeLang[top];
    }

    /**
     * détermine la langue la plus probable
     * @param prob vecteur des probabilités de chaque automate
     * @return numéro de la langue la plus probable
     */
    private int topLang(double[] prob) {
        double max = Double.MAX_VALUE;
        int imax = -1;
        for (int i = 0; i < lastLang; i++) { // for each lang
            if (max > prob[i]) {
                max = prob[i];
                imax = i;
            }
        }
        return imax;
    }
}
