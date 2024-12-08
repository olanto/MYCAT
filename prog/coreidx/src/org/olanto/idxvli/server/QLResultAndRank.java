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
 * Classe stockant les  résultats d'une requête avec ranking.
 * 
 *
 */
public class QLResultAndRank implements Serializable {

    /* les documents résultats */

    /**
     *
     */

    public int[] result;
    /* les poids des résultats */

    /**
     *
     */

    public float[] rank;
    /* les poids des résultats */

    /**
     *
     */

    public String[] docName;
    /* la durée d'exécution */

    /**
     *
     */

    public long duration;  // en ms

    /** crée un résultat
     * @param result id des documents
     * @param rank poids des documents
     * @param duration durée
     */
    public QLResultAndRank(int[] result, float[] rank, long duration) {
        this.result = result;
        this.rank = rank;
        this.duration = duration;
    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        out.writeObject(result);
        out.writeObject(rank);
        out.writeObject(docName);
        out.writeLong(duration);
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        result = (int[]) in.readObject();
        rank = (float[]) in.readObject();
        docName = (String[]) in.readObject();
        duration = in.readLong();
    }
}
