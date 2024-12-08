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

package org.olanto.idxvli.cache;

/**
 *  Comportement du coordinateur des caches pour l'indexation.
 * 
 *
 */
public interface CacheCoordinator {

    /** démarre le timing pour les mesures de performance
     */
    public void startTimer();

    /** incrémente le total des termes indexés
     */
    public void incTotalIdx();

    /** retourne la taille total des caches coordonsés
     * @return 
     */
    public long cacheCurrentSize();

    /** retourne le nombre de termes actifs dans les caches
     * @return 
     */
    public int allocate();

    /** indique si les caches sont à  nettoyer
     * @return 
     */
    public boolean cacheOverFlow();

    /** libère partiellement les caches
     */
    public void freecache();

    /** libère complètement les caches
     */
    public void freecacheFull();
}
