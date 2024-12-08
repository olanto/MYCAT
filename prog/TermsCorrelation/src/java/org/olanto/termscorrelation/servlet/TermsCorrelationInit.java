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

package org.olanto.termscorrelation.servlet;

/**
 * Une classe pour initialiser les constantes.
 * <p>
 * >
 *
 * Une classe pour initialiser les constantes. Cette classe doit peut être implémentée pour chaque application
 */
public interface TermsCorrelationInit {

    /** initialisation permanante des constantes. 
     * Ces constantes choisies de manière permanente pour la segmentation
     */
    public void InitPermanent();

    /** initialisation des constantes de configuration (modifiable). 
     * Ces constantes choisies définitivement pour toute la durée de la vie du processus.
     */
    public void InitConfiguration();
}
