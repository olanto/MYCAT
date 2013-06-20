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

import java.util.*;

/**
 * Une classe pour mémoriser les informations sur les noeuds d'un automate de Markov.
 *
 *
 */
public class MNode {

    /** générateur aléatoir */
    private static Random r = new Random();
    /** poids d'une tansition inconnue.
     * <p>
     * Ce facteur peut être adapté selon les langues et la
     * longueur du texte d'apprentissage
     * <p>
     * 0= la transition est certaine<br>
     * -infini = la transition est impossible<br>
     * -10 = est une valeur raisonable<br>
     * En théorie, la valeur devrait être inférieur à la transition la moins probable
     * de l'automate (une amélioration possible du code ?)
     */
    private static int wgtNotFound = -10; //-10000000;
    /** libellé du noeud */
    private String ref;
    /** nbr total d'occurence du noeud */
    private int totalOcc = 0;
    /** nbr de liens partant du noeud */
    private int nbLinks = 0;
    /** Hash des liens partant de ce noeud */
    private Hashtable<String, MLink> links = new Hashtable<String, MLink>();

    /** crée un noeud avec son libellé
     * @param reference libellé du noeud
     */
    MNode(String reference) {
        ref = reference;
    }

    /** ajoute un lien à ce noeud. Si ce noeud existe déjà, on ajoute seulement une occurrence.
     * @param linkReference libellé du lien
     */
    void addLink(String linkReference) {
        totalOcc++;
        MLink link = links.get(linkReference);
        if (link != null) {// il existe déja
            link.add();
        } else {  // c'est un nouveau
            link = new MLink(linkReference);
            links.put(linkReference, link);
            nbLinks++;
        }
    }

    /** affiche les informations sur ce noeud (dans la console, pour les tests)*/
    void show() {
        System.out.println("from:" + ref + "(" + totalOcc + ")=>");
        for (Enumeration<String> i = links.keys(); i.hasMoreElements();) {
            MLink link = links.get(i.nextElement());
            System.out.println(link.ref + "=" + link.nbOcc);
        }

    }

    /** tire au hasard un item possible depuis ce noeud
     * @return un item suivant possible à partir de ce noeud
     */
    String getnext() {
        if (totalOcc != 0) {
            int h = r.nextInt(totalOcc);
            for (Enumeration<String> i = links.keys(); i.hasMoreElements();) {
                MLink link = links.get(i.nextElement());
                h -= link.nbOcc;
                if (h < 0) {
                    return link.ref;
                }
            }
        }
        return "."; // never go on this !
    }

    /** calcul la probabilité d'une transition de ce noeud vers celui référencé par l'item
     * @param item libellé du noeud vers lequel on veut transiter
     * @return log(probabilité d'une transition de ce noeud vers l'item). Si la transition est inconnue, alors on retourne WgtNotFound.
     */
    double probOfNext(String item) {
        double x, y;
        MLink link = links.get(item);
        if (link != null) {// on a trouvé
            return Math.log((double) link.nbOcc / (double) totalOcc);
        }
        return getWgtNotFound(); // not found
    }

    /** Retourne le nombre de liens partant de ce noeud
     * @return le nombre de liens partant de ce noeud
     */
    public int getNbLinks() {
        return nbLinks;
    }

    /** Retourne la probabilité attribuée aux transitions inconnues
     * @return la probabilité attribuée aux transitions inconnues
     */
    public static int getWgtNotFound() {
        return wgtNotFound;
    }

    /** Modifie la probabilité attribuée aux transitions inconnues
     * @param _wgtNotFound la probabilité attribuée aux transitions inconnues
     */
    public static void setWgtNotFound(int _wgtNotFound) {
        wgtNotFound = _wgtNotFound;
    }
}
