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

package org.olanto.util;

/** 
 * Gestion de timer en nano seconde.
 * Une classe pour déclencher un chronomètre et pour mesurer facilement l'efficacité du code.
 * Par exemple: 
 * <pre> 
 * Timer t1 = new Timer("section A du code");  // le chrono a démarrer!
 *    ...section A ... 
 * t1.stop(); // affiche le temps en milliseconde ...
 *
 */
public class TimerNano {

    private long start;
    private String activity;
    /**
     * crée un chronomètre. Et puis le démarre et affiche dans 
     * la console le commentaire associé
     * 
     * @param s le commentaire associé au chrono. 
     */

    /* valeur en nanosecondes d'un start/stop sur une machine données */
    long EmptyOnThisComputer = 0; // 2500 nano 0=pas de compensation du timer

    public TimerNano(String s, boolean silent) {
        activity = s;
        if (!silent) {
            System.out.println("START: " + activity);
        }
        start = System.nanoTime();
    }

    /**
     * stope le chronomètre. Et affiche dans la console le commentaire associé
     * et le temps mesuré en milliseconde
     * 
     */
    public long stop(boolean silent) {
        start = System.nanoTime() - start - EmptyOnThisComputer;
        if (!silent) {
            System.out.println("STOP: " + activity + " - " + start / 1000 + " us");
        }
        return start / 1000;
    }

    /**
     * redémarre le chronomètre. Et affiche dans la console le commentaire associé
     * @param s le commentaire associé avec le chronomètre
     */
    public void restart(String s, boolean silent) {
        activity = s;
        if (!silent) {
            System.out.println("START: " + activity);
        }
        start = System.nanoTime();
    }
}
