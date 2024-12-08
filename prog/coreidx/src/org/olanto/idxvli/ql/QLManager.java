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

package org.olanto.idxvli.ql;

import org.olanto.idxvli.*;
import org.olanto.conman.server.*;
import org.olanto.idxvli.server.*;

/**
 * Comportement d'un exécuteur de requête.
 * 
 *
 *
 * Comportement d'un exécuteur de requête.
 */
public interface QLManager {

    /**
     * retourne la liste des documents valides correspondants à la requête, (null) si erreur.
     * @param request requête
     * @param id indexeur de référence
     * @return la liste des documents valides
     */
    public int[] get(String request, IdxStructure id);

    /**
     * Init Cache
     */
    public void initCache();
    
    /**
     *
     * @param request
     * @param id
     * @return
     */
    public QLResultAndRank getMore(String request, IdxStructure id);

    /**
     *
     * @param id
     * @param cs
     * @param request
     * @param start
     * @param size
     * @param fullresult
     * @return
     */
    public QLResultNice get(IdxStructure id, ContentService cs, String request, int start, int size, boolean fullresult);

    /**
     *
     * @param id
     * @param cs
     * @param request1
     * @param request2
     * @param start
     * @param size1
     * @param size2
     * @return
     */
    public QLResultNice get(IdxStructure id, ContentService cs, String request1, String request2, int start, int size1, int size2);

    /**
     *
     * @param id
     * @param cs
     * @param request
     * @param properties
     * @param start
     * @param size
     * @return
     */
    public QLResultNice get(IdxStructure id, ContentService cs, String request, String properties, int start, int size);

    /**
     *
     * @param id
     * @param cs
     * @param request
     * @param properties
     * @param profile
     * @param start
     * @param size
     * @param fullresult
     * @return
     */
    public QLResultNice get(IdxStructure id, ContentService cs, String request, String properties, String profile, int start, int size, boolean fullresult);
}
