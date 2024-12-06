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

package org.olanto.idxvli.server;

import java.io.*;

/**
 * Classe stockant les  résultats d'un référencement Quote détection
 * 
 *
 */
public class REFResultNice implements Serializable {

    /**
     *
     */
    public static final String DOC_REF_SEPARATOR = "|";
    /* la position en caractères depuis le début de la référence */

    /**
     *
     */

    public int[] posref;
    /* la liste des documents contenant le texte de la référence */

    /**
     *
     */

    public String[] listofref;
    /* la liste du texte référencé */

    /**
     *
     */

    public String[] reftext;
    /* visualisation des ref en html */

    /**
     *
     */

    public String htmlref;
   /* visualisation des info en xml */

    /**
     *
     */

    public String xmlInfo;
    /* nbr de références */

    /**
     *
     */

    public int nbref;
    /* la durée d'exécution */

    /**
     *
     */

    public long duration;  // en ms

    /**
     *
     */
    public int XMLtotword;

    /**
     *
     */
    public int XMLtotwordref;

    /**
     *
     */
    public String XMLpctref;
    
    /** crée un résultat
     * @param posref

     * @param listofref
     * @param reftext
     * @param htmlref
     * @param duration durée
     * @param nbref
     * @param xmlInfo
     * @param XMLtotword
     * @param XMLtotwordref
     * @param XMLpctref
     */
    public REFResultNice(int[] posref, String[] listofref, String[] reftext, String htmlref, String xmlInfo, int nbref, long duration,
               int XMLtotword, int XMLtotwordref, String XMLpctref) {
        this.posref = posref;
        this.listofref = listofref;
        this.reftext = reftext;
        this.htmlref = htmlref;
      this.XMLtotword = XMLtotword;
      this.XMLtotwordref = XMLtotwordref;
      this.xmlInfo = xmlInfo;
        this.XMLpctref = XMLpctref;
        this.duration = duration;
    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        out.writeObject(posref);
        out.writeObject(listofref);
        out.writeObject(reftext);
        out.writeObject(htmlref);
        out.writeInt(nbref);
        out.writeLong(duration);
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        posref = (int[]) in.readObject();
        listofref = (String[]) in.readObject();
        reftext = (String[]) in.readObject();
        htmlref = (String) in.readObject();
        nbref = in.readInt();
        duration = in.readLong();
    }
}
