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
 * Gestion de timer en milli seconde.
 * Une classe pour déclencher un chronomètre et pour mesurer facilement l'efficacité du code.
 * Par exemple: 
 * <pre> 
 * Timer t1 = new Timer("section A du code");  // le chrono a démarrer!
 *    ...section A ... 
 * t1.stop(); // affiche le temps en milliseconde ...
 *
 */
public class Timer {

    private long start;
    private String activity;

    /**
     * crée un chronomètre. Et puis le démarre et affiche dans 
     * la console le commentaire associé
     * 
     * @param s le commentaire associé au chrono. 
     */
    public Timer(String s) {
        activity = s;
        start = System.currentTimeMillis();
        System.out.println("START: " + activity);
    }

    /**
     * crée un chronomètre. Et puis le démarre et affiche dans 
     * la console le commentaire associé
     * 
     * @param s le commentaire associé au chrono. 
     */
    public Timer(String s, boolean silent) {
        activity = s;
        start = System.currentTimeMillis();
    }

    /**
     * stope le chronomètre. Et affiche dans la console le commentaire associé
     * et le temps mesuré en milliseconde
     * 
     */
    public void stop() {
        start = System.currentTimeMillis() - start;
        System.out.println("STOP: " + activity + " - " + start + " ms");
    }

    /**
     * stope le chronomètre. Et affiche dans la console le commentaire associé
     * et le temps mesuré en milliseconde
     * 
     */
    public long getstop() {
        return System.currentTimeMillis() - start;
    }

    /**
     * redémarre le chronomètre. Et affiche dans la console le commentaire associé
     * @param s le commentaire associé avec le chronomètre
     */
    public void restart(String s) {
        activity = s;
        start = System.currentTimeMillis();
        System.out.println("START: " + activity);
    }
}
